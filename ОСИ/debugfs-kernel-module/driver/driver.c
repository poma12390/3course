#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/module.h>
#include <linux/kdev_t.h>
#include <linux/fs.h>
#include <linux/cdev.h>
#include <linux/device.h>
#include <linux/slab.h>                 // kmalloc()
#include <linux/uaccess.h>              // copy_to/from_user()
#include <linux/debugfs.h>

#include <linux/pid.h>
#include <linux/sched.h>
#include <linux/sched/signal.h>
#include <linux/netdevice.h>
#include <linux/device.h>
#include <linux/pci.h>
#include <linux/mutex.h>


#define BUF_SIZE 1024

MODULE_LICENSE("Dual BSD/GPL");
MODULE_DESCRIPTION("Stab linux module for operating system's lab");
MODULE_VERSION("1.0");
#define MUTEX 1
static DEFINE_MUTEX(rw_mutex);

static int pid = 1;
static int struct_id = 1;

static int filevalue; 
static struct dentry *parent;

/*
** Function Prototypes
*/
static int      __init lab_driver_init(void);
static void     __exit lab_driver_exit(void);


/***************** devugfs Functions *******************/
static ssize_t  read_debug(struct file *filp, char __user *buffer, size_t length,loff_t * offset);
static ssize_t  write_debug(struct file *filp, const char *buff, size_t len, loff_t * off);
static int      open_debug(struct inode *inode, struct file *file);
static int      release_debug(struct inode *inode, struct file *file);

/*
** debugfs operation sturcture
*/
static struct file_operations  debug_fops = {
        .read = read_debug,
        .write = write_debug,
        .open = open_debug, 
        .release = release_debug
};

// net_device
static size_t write_pci_device_struct(char __user *ubuf){
    char buf[BUF_SIZE];
    size_t len = 0;

    static struct pci_dev* dev;

    //read_lock(&dev_base_lock);
    while((dev = pci_get_device(PCI_ANY_ID, PCI_ANY_ID, dev))){
        len += sprintf(buf+len, "pci found [%d]\n",     dev->device);
        //len += sprintf(buf+len, "name = %ld\n", dev->dev.init_name);
        //len += sprintf(buf+len, "type = %ld\n", dev->dev.type->name);

        len += sprintf(buf+len, "vendor = %ld\n", dev->vendor);
        len += sprintf(buf+len, "devfn = %ld\n", dev->devfn);
        len += sprintf(buf+len, "class = %ld\n", dev->class);
        len += sprintf(buf+len, "\n");
        
        printk(KERN_INFO "pci found [%d]\n", dev->device);
    }


    if (copy_to_user(ubuf, buf, len)){
        return -EFAULT;
    }

    return len;
}

// fpu_struct
static size_t write_fpu_state_struct(char __user *ubuf, struct fpstate *fpu_state){
    char buf[BUF_SIZE];
    size_t len = 0;

    //struct fpstate* fpu_state = task_struct_ref->thread.fpu.fpstate;

    //len += sprintf(buf+len, "pid = %d\n", pid);
    //len += sprintf(buf,     "live = %d\n",                  atomic_read(&(signalStruct->live)));
    len += sprintf(buf+len, "size = %d\n", fpu_state->size);
    len += sprintf(buf+len, "user_size = %d\n", fpu_state->user_size);
    len += sprintf(buf+len, "xfeatures = %d\n", fpu_state->xfeatures);
    len += sprintf(buf+len, "user_xfeatures = %d\n", fpu_state->user_xfeatures);
    len += sprintf(buf+len, "xfd = %d\n", fpu_state->xfd);
    len += sprintf(buf+len, "is_valloc = %d\n", fpu_state->is_valloc);
    len += sprintf(buf+len, "is_guest = %d\n", fpu_state->is_guest);
    len += sprintf(buf+len, "is_confidential = %d\n", fpu_state->is_confidential);
    len += sprintf(buf+len, "in_use = %d\n", fpu_state->in_use);

    //TODO: finish

    if (copy_to_user(ubuf, buf, len)){
        return -EFAULT;
    }

    return len;
}

static int open_debug(struct inode *inode, struct file *file)
{
    mutex_lock(&rw_mutex);
    printk(KERN_INFO "debug file opened.....\t");
    return 0;
}

static int release_debug(struct inode *inode, struct file *file)
{
    mutex_unlock(&rw_mutex);
    printk(KERN_INFO "debug file released.....\n");
    return 0;
}


/*
** Эта фануция будет вызвана, когда мы ПРОЧИТАЕМ файл debugfs
*/
static ssize_t read_debug(struct file *filp, char __user *ubuf, size_t count, loff_t *ppos) {

    char buf[BUF_SIZE];
    int len = 0;

    struct task_struct *task_struct_ref = get_pid_task(find_get_pid(pid), PIDTYPE_PID);
    
    printk(KERN_INFO "debug file read.....\n");
    if (*ppos > 0 || count < BUF_SIZE){
        return 0;
    }

    if (task_struct_ref == NULL){
        len += sprintf(buf,"task_struct for pid %d is NULL. Can not get any information\n",pid);

        if (copy_to_user(ubuf, buf, len)){
            return -EFAULT;
        }
        *ppos = len;
        return len;
    }

    switch(struct_id){
        default:
        case 0:
            len = write_pci_device_struct(ubuf);
            break;
        case 1:
            struct fpstate* state = task_struct_ref->thread.fpu.fpstate;
            len = write_fpu_state_struct(ubuf, state);
            break;
    }

    *ppos = len;
    return len;
}

/*
** Эта фануция будет вызвана, когда мы ЗАПИШЕМ в файл debug
*/
static ssize_t write_debug(struct file *filp, const char __user *ubuf, size_t count, loff_t *ppos) {

    int num_of_read_digits, c, a, b;
    char buf[BUF_SIZE];
   

    printk(KERN_INFO "debug file wrote.....\n");

    if (*ppos > 0 || count > BUF_SIZE){
        return -EFAULT;
    }

    if( copy_from_user(buf, ubuf, count) ) {
        return -EFAULT;
    }

    num_of_read_digits = sscanf(buf, "%d %d", &a, &b);
    if (num_of_read_digits != 2){
        return -EFAULT;
    }

    struct_id = a;
    pid = b;

    c = strlen(buf);
    *ppos = c;

    return c;
}

/*
** Функция инициализации Модуля
*/
static int __init lab_driver_init(void) {
    /* Создание директории процесса. Она будет создана в файловой системе "/debug" */
    parent = debugfs_create_dir("lab",NULL);

    if( parent == NULL )
    {
        printk("Error creating debug entry");
        return -1;
    }


    /* Создание записи процесса в разделе "/debugfs/lab/" */
    debugfs_create_file("struct_info", 0666, parent, &filevalue, &debug_fops);
    mutex_init(&rw_mutex);
    printk("Device Driver Insert...Done!!!\n");
    return 0;
}

/*
** Функция выхода из Модуля
*/
static void __exit lab_driver_exit(void)
{
    /* Удаляет 1 запись процесса */
    //remove_proc_entry("lab/struct_info", parent);

    /* Удяление полностью /proc/lab */
    debugfs_remove_recursive(parent);
    mutex_destroy(&rw_mutex);
    
    printk("Device Driver Remove...Done!!!\n");
}

module_init(lab_driver_init);
module_exit(lab_driver_exit);