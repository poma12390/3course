import subprocess
import matplotlib.pyplot as plt
import time
import re
from time import sleep


##
# network: [netlink-task,netdev];
##
def run_stress_ng(command):
    print(command)
    return subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)


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


def network_test():
    for i in range(1, 10, 2):
        sb = run_stress_ng(f'stress-ng --netdev {i} --timeout 30')
        command = 'ip -s link show wlo1'
        timestamps = []
        data = []
        prev_bytes = 0
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

                if prev_bytes == 0:
                    prev_bytes = float(output[3][1]) + float(output[5][1])
                else:
                    bytes = float(output[3][1]) + float(output[5][1])
                    timestamps.append(time.time())
                    data.append(bytes - prev_bytes)
                    prev_bytes = bytes
            sleep(1)
        plot_graph(timestamps, data, f'netdev-{i}', True)
    for i in range(1, 10, 2):
        sb = run_stress_ng(f'stress-ng --netlink-task {i} --timeout 30')
        command = 'ip -s link show wlo1'
        timestamps = []
        data = []
        prev_bytes = 0
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

                if prev_bytes == 0:
                    prev_bytes = float(output[3][1]) + float(output[5][1])
                else:
                    bytes = float(output[3][1]) + float(output[5][1])
                    timestamps.append(time.time())
                    data.append(bytes - prev_bytes)
                    prev_bytes = bytes
            sleep(1)
        plot_graph(timestamps, data, f'netlink-task-{i}', True)


network_test()