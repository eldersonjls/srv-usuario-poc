#!/bin/bash
# Script de migração completa para arquitetura hexagonal
# Preserva TODAS as funcionalidades originais

echo "🔄 Iniciando migração para arquitetura hexagonal..."

# Backup da estrutura hexagonal atual
echo "📦 Criando backup da estrutura hexagonal..."
mkdir -p /tmp/hexagonal-backup
cp -r src/main/java/com/viafluvial/srvusuario/adapters /tmp/hexagonal-backup/
cp -r src/main/java/com/viafluvial/srvusuario/common /tmp/hexagonal-backup/

# Listar funcionalidades a migrar
echo "📋 Funcionalidades encontradas:"
echo "  ✅ Admin (domain/entity/Admin.java)"
echo "  ✅ Agency (domain/entity/Agency.java)"  
echo "  ✅ Approval (domain/entity/Approval.java)"
echo "  ✅ Boatman (domain/entity/Boatman.java)"
echo "  ✅ Passenger (domain/entity/Passenger.java)"
echo "  ✅ Auth (application/service/AuthService.java)"
echo "  ✅ User (já migrado)"

echo ""
echo "📂 Estrutura alvo:"
echo "  domain/model/          (entidades puras)"
echo "  application/port/      (interfaces use case)"
echo "  application/usecase/   (implementações)"
echo "  application/dto/       (DTOs internos)"  
echo "  adapters/in/web/       (controllers REST)"
echo "  adapters/out/persistence/ (JPA adapters)"

echo ""
echo "⚠️  ATENÇÃO: Esta migração manterá TODAS as funcionalidades!"
echo "   Nenhum endpoint será removido."

read -p "Continuar? (s/n) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Ss]$ ]]
then
    echo "❌ Migração cancelada"
    exit 1
fi

echo ""
echo "✅ Para continuar manualmente, execute os seguintes passos:"
echo ""
echo "1. Copiar domain entities (remover annotations JPA):"
echo "   Admin, Agency, Approval, Boatman, Passenger → domain/model/"
echo ""
echo "2. Criar JPA entities separadas em:"
echo "   adapters/out/persistence/entity/"
echo ""
echo "3. Criar ports para cada módulo:"
echo "   application/port/in/  (Admin, Agency, Approval, Boatman, Passenger use cases)"
echo "   application/port/out/ (Repository ports)"
echo ""
echo "4. Migrar Services → UseCases:"
echo "   application/service/* → application/usecase/*"
echo ""
echo "5. Migrar Controllers:"
echo "   presentation/controller/* → adapters/in/web/controller/*"
echo ""
echo "6. Criar persistence adapters:"
echo "   infrastructure/repository/* → adapters/out/persistence/"
echo ""

echo "Script de migração preparado! Execute os passos acima."
