#include <stdlib.h>
#include <fcntl.h>
#include <string.h>

#define BUFF_SIZE 128

int main(int argc, char *argv[]) {
    int file_desc;
    char buff[BUFF_SIZE];

    file_desc = open("/sys/kernel/debug/vmstat_info", O_RDONLY);
    if(file_desc < 0) {
        printf("Could not open the file\n");
        return -1;
    }

    memset(buff, 0, BUFF_SIZE);
    if (read(file_desc, buff, BUFF_SIZE - 1) < 0) {
        printf("Error reading from the file\n");
        close(file_desc);
        return -1;
    }

    printf("%s", buff);
    close(file_desc);
    return 0;
}