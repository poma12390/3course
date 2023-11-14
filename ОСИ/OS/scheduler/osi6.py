import subprocess
import matplotlib.pyplot as plt
import time
import re
from time import sleep
import statistics


##
# sched: [resched,sched-period]
##
def run_stress_ng(command):
    print(command)
    return subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)


# command pass with
# Функция для построения графика
def plot_graph(timestamps, data, name, isList):
    plt.figure(figsize=(10, 6))
    print(data)

    if not isList:
        for sublist in data:
            plt.plot(timestamps, sublist)
            print(sublist)
    else:
        plt.plot(timestamps, data)

    plt.title('System Performance')
    plt.grid(True)
    plt.savefig(name)
    return


def sh_test():
    # c_sw = []
    # for i in range(1, 17, 5):
    #     sp = run_stress_ng(f'sudo perf stat -e context-switches stress-ng --resched {i} --timeout 20')
    #     output = sp.communicate()[1]
    #     output = output.split('\n')

    #     c_s_l = output[6]
    #     print(c_s_l)
    #     c_s_l = re.sub(r'\s+', ' ', c_s_l)
    #     c_s_l = re.sub(r',', '.', c_s_l)
    #     c_s_l = c_s_l.split(' ')
    #     j = 1
    #     switch_num = 0
    #     while(c_s_l[j].isnumeric()):
    #         switch_num = switch_num*1000 + float(c_s_l[j])
    #         j+=1
    #     c_sw.append(switch_num)
    # plot_graph(range(1, 17, 5), c_sw, 'sched', True)

    sb = run_stress_ng(f'stress-ng --hdd 10 --hdd-bytes 1g  --sched-period 1  --timeout 30s --metrics-brief')
    read_speed = 0

    output = subprocess.check_output('iostat 1 30 | grep nvme', shell=True, text=True)

    output = output.split('\n')
    for str in output:
        str = re.sub(r'\s+', ' ', str)
        str = re.sub(r',', '.', str)
        str = str.split(' ')
        if (str != ['']):
            read_speed += float(str[3])
    print('sched time 1:')
    print((read_speed) / 30)

    sb = run_stress_ng(f'stress-ng --hdd 10 --hdd-bytes 1g  --sched-period 100000  --timeout 30s --metrics-brief')
    read_speed = 0

    output = subprocess.check_output('iostat 1 30 | grep nvme', shell=True, text=True)

    output = output.split('\n')
    for str in output:
        str = re.sub(r'\s+', ' ', str)
        str = re.sub(r',', '.', str)
        str = str.split(' ')
        if (str != ['']):
            read_speed += float(str[3])
    print('sched time 100000:')
    print((read_speed) / 30)


sh_test()
