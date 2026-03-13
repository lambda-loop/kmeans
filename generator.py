import numpy as np
import csv

N = 100
D = 5
C = 3
output_file = 'dados_kmeans.csv'
np.random.seed(42)  # para reprodutibilidade

# Gera centróides dos clusters em um hipercubo [-100, 100]
centers = np.random.uniform(low=-100, high=100, size=(C, D))

# Tamanhos dos clusters (distribuição uniforme aproximada)
cluster_sizes = np.random.multinomial(N, [1/C]*C)

# Gera todos os pontos
points = []
for i, size in enumerate(cluster_sizes):
    # Pontos em torno do centróide com desvio padrão 5 (clusters bem separados)
    cluster_points = np.random.normal(loc=centers[i], scale=5.0, size=(size, D))
    points.append(cluster_points)

points = np.vstack(points)
np.random.shuffle(points)  # embaralha a ordem dos clusters

# Escreve no arquivo CSV (sem cabeçalho)
with open(output_file, 'w', newline='') as f:
    writer = csv.writer(f)
    writer.writerows(points)

print(f"Arquivo '{output_file}' gerado com {N} linhas e {D} colunas.")
