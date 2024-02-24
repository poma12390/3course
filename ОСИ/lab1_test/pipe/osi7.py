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

    plt.xlabel("Pipe priority")
    plt.ylabel("Context-switched")
    plt.title('Pipe Performance')
    plt.grid(True)
    plt.savefig(name)
    return

def plot_graph2(timestamps, data, name, isList):
    plt.figure(figsize=(10, 6))
    print(data)

    if not isList:
        for sublist in data:
            plt.plot(timestamps, sublist)
            print(sublist)
    else:
        plt.plot(timestamps, data)

    plt.xlabel("Pipe count")
    plt.ylabel("Context-switches")
    plt.title('Pipe Performance')
    plt.grid(True)
    plt.savefig(name)
    return

def sh_test():
    # c_sw = []
    # for i in range(4, 4097, 500):
    #     sp = run_stress_ng(f'sudo perf stat -e context-switches stress-ng --pipe 2 --pipe-data-size {i}  --timeout 10')
    #     output = sp.communicate()[1]
    #
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
    # plot_graph(range(4, 4097, 500), c_sw, 'pipe-data-size-test', True)

    c_sw = []
    for i in range(1, 8):
        sp = run_stress_ng(f'sudo perf stat -e context-switches stress-ng --pipe {i} --pipeherd-yield  --timeout 10')
        output = sp.communicate()[1]
        output = output.split('\n')
        c_s_l = output[6]
        print(c_s_l)
        c_s_l = re.sub(r'\s+', ' ', c_s_l)
        c_s_l = re.sub(r',', '.', c_s_l)
        c_s_l = c_s_l.split(' ')
        j = 1
        switch_num = 0
        while(c_s_l[j].isnumeric()):
            switch_num = switch_num*1000 + float(c_s_l[j])
            j+=1
        c_sw.append(switch_num)
    plot_graph2(range(1, 8), c_sw, 'pipeherd-yield', True)


sh_test()
