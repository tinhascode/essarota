import sys
import os
import json
import re
import argparse
import urllib.request
import urllib.error
from pathlib import Path

# Cores no terminal
GREEN = "\033[92m"
RED = "\033[91m"
YELLOW = "\033[93m"
CYAN = "\033[96m"
BOLD = "\033[1m"
RESET = "\033[0m"


def carregar_env(caminho_env: Path) -> dict:
    env_vars = {}
    if not caminho_env.exists():
        return env_vars

    with open(caminho_env, "r", encoding="utf-8") as f:
        for linha in f:
            linha = linha.strip()
            if not linha or linha.startswith("#") or "=" not in linha:
                continue
            chave, _, valor = linha.partition("=")
            chave = chave.strip()
            valor = valor.strip().strip("'\"")
            env_vars[chave] = valor
    return env_vars


def normalizar_telefone(telefone: str) -> str:
    numeros = re.sub(r"\D", "", telefone)
    if len(numeros) in (10, 11) and not numeros.startswith("55"):
        numeros = "55" + numeros
    return numeros


def listar_instancias(base_url: str, api_key: str) -> list:
    url = f"{base_url.rstrip('/')}/instance/fetchInstances"
    req = urllib.request.Request(
        url,
        headers={"apikey": api_key, "Content-Type": "application/json"},
        method="GET",
    )
    try:
        with urllib.request.urlopen(req, timeout=10) as resp:
            data = json.loads(resp.read().decode("utf-8"))
            if isinstance(data, list):
                return data
            return []
    except Exception as e:
        print(f"{YELLOW}[AVISO] Não foi possível listar instâncias automaticamente: {e}{RESET}")
        return []


def enviar_mensagem(base_url: str, api_key: str, instancia: str, numero: str, texto: str):
    url = f"{base_url.rstrip('/')}/message/sendText/{instancia}"
    payload = {
        "number": numero,
        "text": texto,
        "delay": 1000,
        "linkPreview": True,
    }

    dados_json = json.dumps(payload).encode("utf-8")
    req = urllib.request.Request(
        url,
        data=dados_json,
        headers={
            "apikey": api_key,
            "Content-Type": "application/json",
            "User-Agent": "Essarota-TestScript/1.0",
        },
        method="POST",
    )

    print(f"\n{CYAN}Enviando requisição para:{RESET} {url}")
    print(f"{CYAN}Destinatário:{RESET} {numero}")
    print(f"{CYAN}Mensagem:{RESET} {texto}\n")

    try:
        with urllib.request.urlopen(req, timeout=15) as resposta:
            codigo = resposta.getcode()
            conteudo = resposta.read().decode("utf-8")
            dados = json.loads(conteudo)
            print(f"{GREEN}{BOLD}✓ Mensagem enviada com sucesso! (HTTP {codigo}){RESET}")
            print(f"\n{BOLD}Resposta da Evolution API:{RESET}")
            print(json.dumps(dados, indent=2, ensure_ascii=False))
            return True

    except urllib.error.HTTPError as e:
        corpo = e.read().decode("utf-8")
        print(f"{RED}{BOLD}✗ Erro ao enviar mensagem (HTTP {e.code}):{RESET}")
        try:
            print(json.dumps(json.loads(corpo), indent=2, ensure_ascii=False))
        except Exception:
            print(corpo)
        return False

    except urllib.error.URLError as e:
        print(f"{RED}{BOLD}✗ Falha de conexão com a Evolution API:{RESET} {e.reason}")
        print(f"{YELLOW}Verifique se a Evolution API está em execução no endereço {base_url}{RESET}")
        return False


def main():
    parser = argparse.ArgumentParser(description="Testar envio de WhatsApp via Evolution API")
    parser.add_argument("-n", "--number", help="Número de WhatsApp com DDD (ex: 11999999999 ou 5511999999999)")
    parser.add_argument("-m", "--message", help="Mensagem de texto a ser enviada")
    parser.add_argument("-i", "--instance", help="Nome da instância na Evolution API")
    parser.add_argument("-u", "--url", help="URL base da Evolution API (padrão: lê do .env ou http://localhost:8081)")
    parser.add_argument("-k", "--apikey", help="API Key da Evolution API (padrão: lê do .env)")
    args = parser.parse_args()

    raiz_projeto = Path(__file__).resolve().parent.parent
    caminho_env = raiz_projeto / ".env"
    env_vars = carregar_env(caminho_env)

    base_url = args.url or env_vars.get("SERVER_URL") or "http://localhost:8081"
    api_key = args.apikey or env_vars.get("AUTHENTICATION_API_KEY") or "evolution_secret_key_change_me"

    print(f"{BOLD}=== Teste de Envio WhatsApp (Evolution API) ==={RESET}")
    print(f"URL Base: {CYAN}{base_url}{RESET}")
    print(f"Arquivo .env: {raiz_projeto / '.env'}\n")

    instancia = args.instance
    if not instancia:
        instancias_encontradas = listar_instancias(base_url, api_key)
        nomes = []
        for inst in instancias_encontradas:
            nome = (
                inst.get("name")
                or inst.get("instanceName")
                or (inst.get("instance", {}).get("instanceName") if isinstance(inst.get("instance"), dict) else None)
            )
            status = (
                inst.get("connectionStatus")
                or (inst.get("instance", {}).get("status") if isinstance(inst.get("instance"), dict) else None)
                or "desconhecido"
            )
            if nome:
                nomes.append((nome, status))

        if nomes:
            print(f"{GREEN}Instâncias encontradas:{RESET}")
            for idx, (nome, status) in enumerate(nomes, start=1):
                print(f"  [{idx}] {nome} (status: {status})")

            if len(nomes) == 1:
                instancia = nomes[0][0]
                print(f"\n{BOLD}Usando automaticamente a única instância conectada:{RESET} {CYAN}{instancia}{RESET}")
            else:
                escolha = input("\nEscolha o número ou digite o nome da instância: ").strip()
                if escolha.isdigit() and 1 <= int(escolha) <= len(nomes):
                    instancia = nomes[int(escolha) - 1][0]
                else:
                    instancia = escolha

        if not instancia:
            instancia = input("Informe o nome da instância: ").strip()

    if not instancia:
        print(f"{RED}Erro: Nome da instância é obrigatório.{RESET}")
        sys.exit(1)

    numero = args.number
    if not numero:
        numero = input("Informe o número de destino (ex: 11999998888 ou 5511999998888): ").strip()

    numero_formatado = normalizar_telefone(numero)
    if not numero_formatado or len(numero_formatado) < 10:
        print(f"{RED}Erro: Número de telefone inválido ('{numero}').{RESET}")
        sys.exit(1)

    mensagem = args.message
    if not mensagem:
        mensagem = input("Digite a mensagem de teste [pressione Enter para usar o padrão]: ").strip()
        if not mensagem:
            mensagem = "🚀 Olá! Esta é uma mensagem de teste enviada pela Evolution API conectada ao Essarota."

    sucesso = enviar_mensagem(
        base_url=base_url,
        api_key=api_key,
        instancia=instancia,
        numero=numero_formatado,
        texto=mensagem,
    )

    if not sucesso:
        sys.exit(1)


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print(f"\n{YELLOW}Operação cancelada pelo usuário.{RESET}")
        sys.exit(0)
