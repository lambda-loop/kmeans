
# AI GENERATED

import numpy as np
import os

# Configurações do dataset
DIMENSOES = 50
CLUSTERS = 3
ARQUIVO_SAIDA = "big_data_50d.csv"
TAMANHO_ALVO_BYTES = 1024 # 1 GB
LINHAS_POR_LOTE = 50_000

# Definindo centros arbitrários para o K-means ter o que procurar
# Ex: Cluster 0 em torno de 0.0, Cluster 1 em torno de 10.0, Cluster 2 em torno de 50.0
centros = [0.0, 10.0, 50.0]

def gerar_lote():
    # Escolhe aleatoriamente a qual cluster cada linha do lote vai pertencer
    escolhas_cluster = np.random.randint(0, CLUSTERS, size=LINHAS_POR_LOTE)
    
    # Cria os dados baseados no centro do cluster + um ruído aleatório (distribuição normal)
    dados = np.zeros((LINHAS_POR_LOTE, DIMENSOES))
    for i in range(CLUSTERS):
        mascara = (escolhas_cluster == i)
        qtd = np.sum(mascara)
        if qtd > 0:
            # np.random.randn gera ruído. Multiplicamos por 2 para espalhar um pouco.
            dados[mascara] = centros[i] + (np.random.randn(qtd, DIMENSOES) * 2.0)
            
    return dados

def main():
    print(f"Iniciando geração de {ARQUIVO_SAIDA} (~1GB)...")
    bytes_escritos = 0
    lotes_gerados = 0
    
    # Abre o arquivo em modo append ('a')
    with open(ARQUIVO_SAIDA, 'a') as f:
        while bytes_escritos < TAMANHO_ALVO_BYTES:
            dados = gerar_lote()
            
            # Converte o array numpy para string CSV em memória e escreve
            # fmt='%.6f' garante 6 casas decimais
            np.savetxt(f, dados, delimiter=',', fmt='%.6f')
            
            # Atualiza o tamanho do arquivo para saber quando parar
            bytes_escritos = os.path.getsize(ARQUIVO_SAIDA)
            lotes_gerados += 1
            
            progresso = (bytes_escritos / TAMANHO_ALVO_BYTES) * 100
            print(f"Progresso: {progresso:.1f}% ({bytes_escritos / (1024*1024):.1f} MB)", end='\r')

    print(f"\nConcluído! Foram gerados {lotes_gerados * LINHAS_POR_LOTE} vetores de dimensão {DIMENSOES}.")

if __name__ == '__main__':
    # Garante que o arquivo comece vazio
    if os.path.exists(ARQUIVO_SAIDA):
        os.remove(ARQUIVO_SAIDA)
    main()
