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
            print(len(output))
            if (len(output) == thread_num + 1):
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
    print(timestamps)
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


def io_metrics():
    # for i in range(1, 52, 10):
    #     sp = run_stress_ng(f'stress-ng --iomix {i} --timeout 30')
    #     read_system_data(sp, 'iotop -b -P -n 1 | grep -m 123 stress-ng',6, f'iomix-{i}', i)
    for i in range(1, 52, 10):
        sp = run_stress_ng(f'stress-ng --ioport {i} --timeout 30')
        read_system_data(sp, 'iotop -b -P -n 1 | grep -m 123 stress-ng', 6, f'ioport-{i}', i)


io_metrics()