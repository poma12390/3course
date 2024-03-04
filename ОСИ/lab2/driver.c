#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/debugfs.h>
#include <linux/sched.h>
#include <linux/mm.h>

MODULE_LICENSE("GPL");

static struct dentry *file;
static char kern_buf[128];

static ssize_t my_read(struct file *fp, char __user *user_buffer, size_t length, loff_t *position)
{
    struct sysinfo i;
    si_meminfo(&i);
    sprintf(kern_buf, "TotalRAM: %lu, FreeRAM: %lu\n", i.totalram, i.freeram);
    return simple_read_from_buffer(user_buffer, length, position, kern_buf, strlen(kern_buf));
}

static ssize_t my_write(struct file *fp, const char __user *user_buffer, size_t length, loff_t *position)
{

    return length;
}

static const struct file_operations my_fops = {
    .read = my_read,
    .write = my_write
};

static int __init dbfs_module_init(void)
{
    file = debugfs_create_file("vmstat_info", 0644, NULL, NULL, &my_fops);
    return 0;
}

static void __exit dbfs_module_exit(void)
{
    debugfs_remove(file);
}

module_init(dbfs_module_init);
module_exit(dbfs_module_exit)