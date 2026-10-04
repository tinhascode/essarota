set -e

if [ -f ".env" ]; then
  export $(grep -v '^#' .env | xargs)
fi

BASE_URL="${SERVER_URL:-http://localhost:8081}"
API_KEY="${AUTHENTICATION_API_KEY:-evolution_secret_key_change_me}"

INSTANCIA="${1:-}"
NUMERO="${2:-}"
MENSAGEM="${3:-Teste de envio via Evolution API no Essarota!}"

if [ -z "$INSTANCIA" ] || [ -z "$NUMERO" ]; then
  echo "Uso: $0 <NOME_DA_INSTANCIA> <NUMERO_COM_DDD> [MENSAGEM]"
  echo "Exemplo: $0 essarota_bot 5511999998888 \"Teste de mensagem\""
  exit 1
fi


NUMERO_LIMPO=$(echo "$NUMERO" | tr -cd '0-9')

if [ ${#NUMERO_LIMPO} -eq 10 ] || [ ${#NUMERO_LIMPO} -eq 11 ]; then
  NUMERO_LIMPO="55$NUMERO_LIMPO"
fi

echo "=========================================="
echo "Enviando mensagem via Evolution API..."
echo "URL:       $BASE_URL/message/sendText/$INSTANCIA"
echo "Para:      $NUMERO_LIMPO"
echo "Mensagem:  $MENSAGEM"
echo "=========================================="

curl -s -X POST "$BASE_URL/message/sendText/$INSTANCIA" \
  -H "apikey: $API_KEY" \
  -H "Content-Type: application/json" \
  -d "{
    \"number\": \"$NUMERO_LIMPO\",
    \"text\": \"$MENSAGEM\",
    \"delay\": 1000,
    \"linkPreview\": true
  }" | jq . || cat

echo ""
