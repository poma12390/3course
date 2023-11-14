import subprocess
import matplotlib.pyplot as plt
import time
import re
from time import sleep


##
# cpu: [union,phi];
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


def cpu_metrics():
    bogops = []
    bogopsps = []
    for i in range(1, 20, 2):
        sp = run_stress_ng(f'stress-ng --cpu {i} --cpu-method union --timeout 20 --metrics-brief')
        output = read_system_data(sp, 'top -b -n 1 | grep -m 123 stress-ng', 9, f'cpu-union-{i}', i)
        output = output.split('\n')
        if len(output) > 5:
            output = output[5]
            output = re.sub(r'\s+', ' ', output)
            output = output.split(' ')
            bogops.append(float(output[4]))
            bogopsps.append(float(output[8]))
        else:
            print(f"Insufficient data in output for i = {i}")
        #output = output[5]
        #output = re.sub(r'\s+', ' ', output)
        #output = output.split(' ')
        #bogops.append(float(output[4]))
        #bogopsps.append(float(output[8]))
    plot_graph([i for i in range(1, 20, 2)], bogops, 'bogops-union', True)
    plot_graph([i for i in range(1, 20, 2)], bogopsps, 'bogopsps-union', True)
    bogops = []
    bogopsps = []

    for i in range(1, 20, 2):
        sp = run_stress_ng(f'stress-ng --cpu {i} --cpu-method phi --timeout 20 --metrics-brief')
        output = read_system_data(sp, 'top -b -n 1 | grep -m 123 stress-ng', 9, f'cpu-phi-{i}', i)
        output = output.split('\n')
        output = output[5]
        output = re.sub(r'\s+', ' ', output)
        output = output.split(' ')
        bogops.append(float(output[4]))
        bogopsps.append(float(output[8]))
    plot_graph([i for i in range(1, 20, 2)], bogops, 'bogops-phi', True)
    plot_graph([i for i in range(1, 20, 2)], bogopsps, 'bogopsps-phi', True)


cpu_metrics()
