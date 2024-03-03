import matplotlib.pyplot as plt

# Последовательность битов
bit_sequence = '110010001010111010001101'

# Преобразование строки в список из 0 и 1
bit_list = [int(bit) for bit in bit_sequence]

# Создание координат для x оси, равных индексам списка
x_coords = list(range(len(bit_list)))

# Построение графика
plt.figure(figsize=(10, 2))
plt.plot(x_coords, bit_list, drawstyle='steps-pre')
plt.yticks([0, 1])
plt.ylim(-0.2, 1.2)

# Удаление лишних осей
plt.gca().spines['left'].set_visible(False)
plt.gca().spines['right'].set_visible(False)
plt.gca().spines['top'].set_visible(False)

# Отображение графика
plt.show()
