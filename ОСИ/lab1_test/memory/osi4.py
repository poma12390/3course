import subprocess
import matplotlib.pyplot as plt
import time
import re
from time import sleep


##
# memory: [misaligned-method,mmaphuge-mmaps];
# Параметры для memory: memrate,memthras
##
def run_stress_ng(command):
    print(command)
    return subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)


def read_system_data(sb, command, col_index, name, thread_num):
    data = [[] for _ in range(thread_num)]
    timestamps = []
    while sb.poll() is None:
        try:
            output = subprocess.check_output(command, shell=True, text=True)
        except Exception as e:
            break
        if (output):
            output = output.split('\n')
            new_output = []
            for index, row in enumerate(output):
                if (row != ''):
                    row = re.sub(r'\s+', ' ', row)
                    row = re.sub(r',', '.', row)
                    row = row.split(' ')
                    new_output.append(row)
            output = new_output
            output = sorted(output, key=lambda x: x[1])
            output = [x for x in output if float(x[col_index]) != 0]
            print(len(output))
            if (len(output) == thread_num):
                print(output)
                timestamps.append(time.time())
                for i in range(thread_num):
                    data[i].append(float(output[i][col_index]))
        sleep(1)

    output = sb.communicate()[1]
    plot_graph(timestamps, data, name, False)
    return output


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


def plot_memory_usage(name, X, Y):
    X_FL = []
    Y_FL = []
    leng = len(X)
    for i in range(leng):
        try:
            X_I = float(X[i])
            Y_I = float(Y[i])
            X_FL.append(X_I)
            Y_FL.append(Y_I)
        except Exception as e:
            continue
    print(X_FL)
    print(Y_FL)
    X_FL = list(map(float, X_FL))
    Y_FL = list(map(float, Y_FL))
    Y_ABS = []
    new_leng=len(X_FL)
    for i in range(new_leng):
        Y_ABS.append(X_FL[i] * Y_FL[i])
    plt.figure(figsize=(10, 6))
    plt.plot(X_FL, Y_FL)
    plt.title('Memory perfomance')
    plt.grid(True)
    plt.savefig(name)

    plt.figure(figsize=(10, 6))
    plt.plot(X_FL, Y_ABS)
    plt.title('Memory perfomance absolute')
    plt.grid(True)
    plt.savefig(name + " abs")


def mem_metrics():
    iarrs = []
    wr1024 = []
    wr512 = []
    for i in range(1, 64, 4):
        sp = run_stress_ng(f'stress-ng --memrate {i} --timeout 10 --metrics-brief')
        output = read_system_data(sp, 'top -b -n 1 | grep -m 123 stress-ng', 8, f'memrate-{i}', i)
        output = output.split('\n')
        iarrs.append(i)
        size2 = len(output[2].split(" "))
        size3 = len(output[3].split(" "))
        print(output[2].split(" ")[size2 - 2])
        wr1024.append(output[2].split(" ")[size2 - 2])
        wr512.append(output[3].split(" ")[size3 - 2])
    print(iarrs)
    print(wr1024)
    print(wr512)

    for i in range(1, 16, 2):
        sp = run_stress_ng(f'stress-ng --memthrash {i} --timeout 60 --metrics-brief')
        read_system_data(sp, 'top -b -n 1 | grep -m 123 stress-ng', 8, f'memthrash-{i}', i)


mem_metrics()
