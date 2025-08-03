# Spring RpgGame
Programa em Java com SpringBoot e React + Vite que simula um sistema de batalhas de RPG(Role-playing Game).

# Funcionamento do Jogo
## Criação de personagem
### Escolha da Classe
 A classe define o estilo de combate do personagem e fornece bônus únicos que 
são ativados durante os ataques bem-sucedidos.
- Berserk <br>
 Bônus:  vida máxima e força. <br>
 Efeito Especial: ao acertar um ataque, tem uma leve chance de causar o dobro 
de dano total.
- Duelista <br>
 Bônus: força e  velocidade. <br>
 Efeito Especial: ao acertar um ataque, tem uma leve chance de rolar uma vez 
extra o dano da arma.
- Paladino <br>
 Bônus: vida e  defesa. <br>
Efeito Especial: ao acertar um ataque, tem uma leve chance de recuperar 
pontos de vida igual ao dano causado.

### Escolha da Raça
 A raça representa a origem do personagem e concede modificadores 
permanentes nos atributos.
 - Humano <br>
 Bônus: poucos pontos em todos os atributos.
-  Anão <br>
 Bônus: vida máxima e defesa.
 - Elfo <br>
 Bônus:  velocidade.
 - Orc <br>
 Bônus:  força. 
### Escolha da Arma
 A arma define o tipo de dado de dano que o personagem irá usar nos ataques baseados em rolagem de dados. <br>
 ( Exemplo: 2d6 = rodar um dado de 6 lados duas vezes.)
 - Machado<br>
 Dano: 2 rolagens de dado de 6 lados 2d6.
 - Martelo<br>
Dano: 1 rolagem de dado de 12 lados 1d12.
 - Adaga<br>
 Dano: 4 rolagens de dado de 3 lados 4d3.
 - Espada<br>
 Dano: 3 rolagens de dado de 4 lados 3d4.

## Atributos dos Personagens
 Cada personagem possui uma pontuação em cinco atributos principais:
 - Vida <br>
 Representa a quantidade máxima de dano que o personagem pode sofrer antes 
de ser derrotado.
 Quando a Vida chega a zero, o personagem é considerado derrotado.
 - Força <br>
 Determina o poder ofensivo do personagem.
 Ao realizar um ataque bem-sucedido, os pontos de Força são somados ao 
resultado do dado de dano, aumentando o impacto do golpe.
 - Velocidade <br>
 Possui o efeito de Iniciativa. <br>
Iniciativa: define quem começa o combate. O personagem com maior 
Velocidade age primeiro.
- Armadura <br>
 Indica o nível de proteção passiva do personagem.
 Durante um ataque, o oponente deve rolar um valor igual ou maior que a 
Armadura para que o golpe seja bem-sucedido. Caso contrário ele erra o golpe, e 
nenhum dano é contabilizado no oponente.

# Como configurar front-end:
- cd front-end
- npm install
- npm install react-router-dom
- npm install jwt-authentication
- npm install axios
- npm install jwt-decode
- npm run dev
- porta: http://localhost:5173

# Como configurar back-end:
- pasta src/main/java/br/ifsp/web
- rodar DemoAuthAppApplicationTests
# Login
### É necessário criar uma conta para entrar no sistema. <br>
<img width="421" height="660" alt="image" src="https://github.com/user-attachments/assets/82f3ea40-be73-4e5a-9362-3449d9fdfdea" />

# Interface
Assim que o login for efetuado, a tela inicial terá as sequintes opções:
- Criar Personagem -> Tela para criação de personagens. <br>
  <img width="691" height="788" alt="image" src="https://github.com/user-attachments/assets/54460629-9565-4b93-8ed3-42bef030c4ad" />
  
- Ver Personagens Criados -> Tela para visualização de personagens. <br>
<img width="971" height="686" alt="image" src="https://github.com/user-attachments/assets/f4e66aa0-79f5-4590-b251-cc00d80bc90d" />

- Iniciar Combate -> Tela para iniciar combate entre dois personagens. Após iniciar o combate, um historico automático dos turnos contendo o dano de cada personagem irá ser construido em tempo real. Quando um personagem tiver a vida menor ou igual a zero o oponente será declarado vencedor. <br>
  <img width="887" height="859" alt="image" src="https://github.com/user-attachments/assets/b6f8125e-14a4-465b-830f-f6d5abf003e7" />
<img width="863" height="505" alt="image" src="https://github.com/user-attachments/assets/f1b76391-3d68-4d45-94cb-9acd0805d7ec" />

- Histórico de Combates -> Tela para visualizar o historico de combates já efetuados. <br>
  <img width="846" height="503" alt="image" src="https://github.com/user-attachments/assets/e944609f-8430-4446-85e1-06ce0faff206" />

  
