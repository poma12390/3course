import subprocess
import matplotlib.pyplot as plt
import time
import re
from time import sleep


##
# io: [ioport,iomix];
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
            print(output)
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
            timestamps.append(time.time())
            print(output)

            for i in range(thread_num):
                data[i].append(float(output[2][col_index]))
        sleep(1)

    output = sb.communicate()[1]
    plot_graph(timestamps, data, name, False)
    return data


def plot_graph(timestamps, data, name, isList):
    plt.figure(figsize=(10, 6))
    print(data)
    if not isList:
        for sublist in data:
            plt.plot(timestamps, sublist)
            print(sublist)
    else:
        plt.plot(timestamps, data)

    plt.title('IO Performance')
    plt.grid(True)
    plt.savefig(name)
    return


def plot_IO_usage(name, X, Y):
    Y_ABS = []
    for i in range(len(X)):
        Y_ABS.append(X[i] * Y[i])
    print(X)
    print(Y)
    print(Y_ABS)
    plt.figure(figsize=(10, 6))
    plt.plot(X, Y_ABS)
    plt.title('IO perfomance absolute')
    plt.grid(True)
    plt.savefig(name + " abs")


def io_metrics():
    iars = []
    Y_AVG = []
    # for i in range(1, 8):
    #     sp = run_stress_ng(f'stress-ng --io 1 --ioport {i} --timeout 10')
    #     kq=read_system_data(sp, 'iotop -b -P -n 1 | grep -m 123 stress-ng',10, f'ioport-{i}', i)
    #     print(kq[0])
    #     Y_AVG.append(sum(kq[0]) / len(kq[0]))
    #     iars.append(i)
    # plot_IO_usage('absolute ioport perf', iars, Y_AVG)

    for i in range(1, 8):
        sp = run_stress_ng(f'stress-ng --io 1 --io-uring {i} --timeout 10')
        kq = read_system_data(sp, 'iotop -b -P -n 1 | grep -m 123 stress-ng', 10, f'io-uring-{i}', i)
        Y_AVG.append(sum(kq[0]) / len(kq[0]))
        iars.append(i)
    plot_IO_usage('absolute io-uring perf', iars, Y_AVG)


io_metrics()
