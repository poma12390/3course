import subprocess
import matplotlib.pyplot as plt
import time
import re
from time import sleep

##
# cache: [l1cache-ssize,cache-level];
##
def run_stress_ng(command):
    print(command)
    return subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)


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


def cache_metrics():

    # cache_misses_percent = []
    # cache_references_num = []
    # for i in range(1, 17, 2):
    #     sp = run_stress_ng(f'sudo perf stat -e cache-references,cache-misses stress-ng --cache 1 --cache-level {i} --timeout 20')
    #     output = sp.communicate()[1]
    #     output = output.split('\n')
    #     refs = output[6]
    #     misses = output[7]
    #     refs = re.sub(r'\s+', ' ', refs)
    #     misses = re.sub(r'\s+', ' ', misses)
    #     misses= re.sub(r',', '.', misses)
    #     refs = re.sub(r',', '.', refs)
    #     refs = refs.split(' ')
    #     misses = misses.split(' ')

    #     print(refs)
    #     print(misses)
    #     cache_misses_percent.append(float(misses[-7]))
    #     j = 1
    #     refs_count = 0
    #     while(refs[j].isnumeric()):
    #         refs_count = refs_count*1000 + float(refs[j])
    #         j+=1
    #     cache_references_num.append(refs_count)
    # plot_graph([i for i in range(1, 17, 2)], cache_references_num, 'cache-level-refs', True)
    # plot_graph([i for i in range(1, 17, 2)], cache_misses_percent, 'cache-level-misses', True)

    cache_misses_percent = []
    cache_references_num = []
    for i in range(1048576, 8388608, 1048576):
        sp = run_stress_ng(
            f'sudo perf stat -e cache-references,cache-misses stress-ng --cache 1 --l1cache-size {i} --timeout 20')
        output = sp.communicate()[1]
        print(output)
        output = output.split('\n')
        refs = output[6]
        misses = output[7]
        refs = re.sub(r'\s+', ' ', refs)
        misses = re.sub(r'\s+', ' ', misses)
        misses = re.sub(r',', '.', misses)
        refs = re.sub(r',', '.', refs)
        refs = refs.split(' ')
        misses = misses.split(' ')

        print(refs)
        print(misses)
        cache_misses_percent.append(float(misses[-7]))
        j = 1
        refs_count = 0
        while (refs[j].isnumeric()):
            refs_count = refs_count * 1000 + float(refs[j])
            j += 1
        cache_references_num.append(refs_count)
        print(refs_count)
        print(float(misses[-7]))
    plot_graph([i for i in range(1048576, 8388608, 1048576)], cache_references_num, 'l1-refs', True)
    plot_graph([i for i in range(1048576, 8388608, 1048576)], cache_misses_percent, 'l1-misses', True)


cache_metrics()
