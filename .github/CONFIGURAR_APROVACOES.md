# 🔒 Como Configurar Aprovações (Substitui Taiga)

## 🎯 Objetivo

Este guia mostra como configurar as **aprovações obrigatórias** no GitHub para substituir os tickets do Taiga no fluxo de deploy.

---

## 📊 Comparação: Antes vs Depois

### ❌ **Fluxo Atual (com Taiga)**

```
Dev → Push staging → CI Pipeline
  ↓
Abrir Ticket Taiga → Operações vê no Taiga
  ↓
Operações faz deploy MANUAL
  ↓
Testes em homologação
  ↓
E-mail para cliente
  ↓
Abrir Ticket Taiga → Operações cria tag
  ↓
Operações faz deploy MANUAL em produção
```

**Problemas:**
- 2 tickets manuais
- Operações executa comandos manualmente
- Possibilidade de erro humano
- Tempo: horas ou dias

---

### ✅ **Novo Fluxo (GitHub Actions)**

```
Dev → Push staging → CI Pipeline AUTOMÁTICA
  ↓
GitHub notifica Operações (e-mail/Slack)
  ↓
Operações APROVA no GitHub (1 clique)
  ↓
Deploy AUTOMÁTICO em homologação
  ↓
Testes em homologação
  ↓
Dev → Merge main + informa versão no PR
  ↓
GitHub notifica Operações + Cliente
  ↓
Operações/Cliente APROVAM no GitHub (1 clique cada)
  ↓
Deploy AUTOMÁTICO em produção + Tag + Release
```

**Benefícios:**
- 0 tickets Taiga
- Deploy completamente automático após aprovação
- Sem erros manuais
- Tempo: minutos
- Rastreabilidade completa

---

## 🔧 Passo a Passo: Configurar Aprovações

### **1️⃣ Configurar Aprovação para HOMOLOGAÇÃO**

Isso substitui o **primeiro ticket do Taiga** (solicitar deploy em homologação).

#### Passos:

1. Vá no repositório do GitHub
2. Clique em **Settings** (⚙️)
3. No menu lateral, clique em **Environments**
4. Clique no environment **staging**
5. Marque a opção **Required reviewers**
6. Clique em **Add reviewer**
7. Adicione os membros da **Equipe de Operações**:
   ```
   Exemplo:
   - @joao-operacoes
   - @maria-operacoes
   - @equipe-ops (se tiver um team)
   ```
8. Configure **Wait timer** (opcional):
   - Se quiser um delay mínimo antes de aprovar (ex: 5 minutos para revisão)
9. Marque **Restrict deployments to selected branches**:
   - Adicione **staging** como branch permitida
10. Clique em **Save protection rules** ✅

#### Resultado:
Quando houver push para `staging`, a pipeline:
1. ✅ Executa testes automaticamente
2. ✅ Faz build automaticamente
3. ⏸️ **PARA e aguarda aprovação da Operações**
4. 📧 GitHub envia **e-mail/notificação** para os aprovadores
5. 👥 Operações vê um botão **"Review deployments"**
6. ✅ Operações clica em **"Approve and deploy"**
7. 🚀 Deploy acontece **automaticamente**

**Não precisa mais abrir ticket no Taiga!** 🎉

---

### **2️⃣ Configurar Aprovação para PRODUÇÃO**

Isso substitui o **segundo ticket do Taiga** (solicitar deploy em produção).

#### Passos:

1. Vá no repositório do GitHub
2. **Settings** → **Environments**
3. Clique no environment **production**
4. Marque **Required reviewers**
5. Adicione **MÚLTIPLOS aprovadores**:
   ```
   Exemplo:
   - @joao-operacoes
   - @maria-operacoes
   - @cliente-po (se o cliente tiver acesso ao GitHub)
   ```
6. **IMPORTANTE**: Defina **quantos aprovadores são necessários**:
   - Se adicionar 3 pessoas e quiser que **todas aprovem**: deixe padrão
   - Se quiser que **apenas 1 das 3 aprove**: configure isso
7. Marque **Restrict deployments to selected branches**:
   - Adicione **main** como branch permitida
8. *(Opcional)* Configure **Deployment branches and tags**:
   - Pode restringir para aceitar apenas tags `v*`
9. **Save protection rules** ✅

#### Resultado:
Quando houver merge para `main` com versão no PR:
1. ✅ Pipeline executa testes e build
2. ⏸️ **PARA e aguarda aprovação dupla** (Operações + Cliente)
3. 📧 GitHub notifica **todos os aprovadores**
4. 👥 Operações revisa e aprova
5. 👥 Cliente revisa e aprova (pode ser direto no e-mail!)
6. 🚀 Após **todas as aprovações**, deploy automático
7. 🏷️ Tag criada automaticamente
8. 📋 Release notes gerado automaticamente

**Elimina e-mails soltos e segundo ticket do Taiga!** 🎉

---

## 📧 Como os Aprovadores São Notificados

### Notificações Automáticas do GitHub:

1. **E-mail**:
   ```
   Assunto: [repo] Deployment waiting for your review
   
   The workflow run "Pipeline Staging" is waiting for your review
   before it can proceed to the staging environment.
   
   [Review pending deployments]  ← Botão que leva direto para aprovar
   ```

2. **Interface do GitHub**:
   - Aparece um banner amarelo na página do workflow
   - Botão grande **"Review pending deployments"**
   - Mostra quem são os aprovadores pendentes

3. **GitHub Mobile App**:
   - Notificação push no celular
   - Pode aprovar direto do app! 📱

### Integração com Slack (opcional):

Você pode configurar notificações no Slack:

```yaml
# Adicionar no workflow
- name: Notificar Slack
  if: always()
  run: |
    curl -X POST ${{ secrets.SLACK_WEBHOOK }} \
      -H 'Content-Type: application/json' \
      -d '{
        "text": "🚀 Deploy aguardando aprovação da Operações!",
        "blocks": [
          {
            "type": "section",
            "text": {
              "type": "mrkdwn",
              "text": "*Pipeline Staging* precisa de aprovação\n\n✅ Testes: Passou\n✅ Build: Concluído\n\n<${{ github.server_url }}/${{ github.repository }}/actions/runs/${{ github.run_id }}|Ver detalhes>"
            }
          }
        ]
      }'
```

---

## 👥 Como Aprovar (Guia para Operações/Cliente)

### **Você recebeu uma notificação?**

#### Opção 1: **Aprovar por E-mail** (mais rápido)
1. Abra o e-mail do GitHub
2. Clique no botão **"Review pending deployments"**
3. Revisa os detalhes do pipeline
4. Clica em **"Approve and deploy"** ou **"Reject"**
5. Pronto! ✅

#### Opção 2: **Aprovar no GitHub**
1. Vá no repositório do GitHub
2. Clique na aba **Actions**
3. Veja o workflow com ícone amarelo ⏸️
4. Clique nele
5. Clique no botão verde **"Review pending deployments"**
6. Marque o(s) environment(s) para aprovar
7. Adicione um comentário (opcional): "Aprovado para homologação - testes OK"
8. Clique em **"Approve and deploy"** ✅

#### Opção 3: **Aprovar pelo App Mobile**
1. Abra o app do GitHub
2. Veja a notificação
3. Toque nela
4. Toque em **"Review"**
5. Aprove! ✅

### **O que revisar antes de aprovar?**

Checklist:
- ✅ Testes passaram (backend + frontend)?
- ✅ SonarQube passou no quality gate?
- ✅ Build foi bem-sucedido?
- ✅ Release notes faz sentido (o que está subindo)?
- ✅ Não há nada suspeito nos logs?

Se tudo OK → **Approve**  
Se algo errado → **Reject** (e comenta o motivo)

---

## 🎯 Resumo dos Benefícios

| Métrica | Antes (Taiga) | Depois (GitHub) |
|---------|---------------|-----------------|
| **Tickets por deploy** | 2 | 0 |
| **Cliques para aprovar** | Abrir browser → Taiga → Criar ticket → Preencher form | 1 clique no e-mail |
| **Deploy manual** | Operações executa comandos | Automático |
| **Notificações** | Não há (precisa checar Taiga) | E-mail + Web + Mobile |
| **Rastreabilidade** | Taiga + GitLab separados | Tudo no GitHub |
| **Tempo de aprovação** | Horas (até Op ver o ticket) | Minutos (notificação imediata) |
| **Erros humanos** | Possíveis (comandos manuais) | Zero (automatizado) |
| **Auditoria** | Difícil | Completa (quem, quando, por quê) |

---

## ❓ FAQ

**P: E se o cliente não tiver conta no GitHub?**  
R: Duas opções:
1. Crie uma conta gratuita para ele (recomendado - pode aprovar por e-mail)
2. Configure apenas Operações como aprovador, e Operações valida com cliente externamente

**P: E se a pessoa que precisa aprovar estiver de férias?**  
R: Configure **múltiplos aprovadores** e defina que **apenas 1 precisa aprovar**. Assim, qualquer um da equipe pode aprovar.

**P: Posso ver o histórico de quem aprovou?**  
R: Sim! No workflow, clique em "Review" e veja todo o histórico de aprovações/rejeições com timestamps e comentários.

**P: Posso cancelar um deploy depois de aprovar?**  
R: Sim! Clique nos 3 pontinhos (...) no workflow e escolha "Cancel workflow run".

**P: Preciso aprovar TODA vez?**  
R: Sim, isso garante controle. Mas se quiser automatizar 100%, pode remover os required reviewers.

**P: E se eu rejeitar por engano?**  
R: Pode re-executar o workflow manualmente (Actions → Pipeline → Re-run jobs).

---

## 📝 Checklist de Configuração

Use este checklist para garantir que está tudo configurado:

### Homologação (Staging):
- [ ] Environment "staging" criado
- [ ] Required reviewers configurados (Equipe de Operações)
- [ ] Branch "staging" adicionada às branches permitidas
- [ ] Aprovadores testaram receber notificação
- [ ] Aprovadores testaram aprovar pelo menos uma vez

### Produção:
- [ ] Environment "production" criado
- [ ] Required reviewers configurados (Operações + Cliente)
- [ ] Branch "main" adicionada às branches permitidas
- [ ] Aprovadores testaram receber notificação
- [ ] Aprovadores testaram aprovar pelo menos uma vez
- [ ] Tag criada automaticamente após deploy

### Comunicação:
- [ ] Time avisado sobre a mudança de processo
- [ ] Guia de aprovação enviado para Operações e Cliente
- [ ] Slack/Teams configurado para notificações (opcional)

---

## 🚀 Próximos Passos

1. **Configure os environments** seguindo este guia
2. **Faça um deploy de teste** em staging
3. **Valide o fluxo de aprovação** com a equipe
4. **Documente diferenças** específicas do seu time (se houver)
5. **Migre 100%** eliminando uso do Taiga para deploys

---

**🎉 Pronto! Seu fluxo de aprovação está modernizado e automatizado!**

Se tiver dúvidas, consulte a [documentação oficial do GitHub](https://docs.github.com/en/actions/deployment/targeting-different-environments/using-environments-for-deployment).
