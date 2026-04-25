# Sistema de Cadastro e Notificação de Eventos

Sistema em Java para cadastro e consulta de eventos da cidade.

---

## Estrutura do Projeto

```
sistema-eventos/
├── Main.java
├── model/
│   ├── Usuario.java
│   └── Evento.java
├── view/
│   └── MenuView.java
└── controller/
    └── EventoController.java
```

---

## Requisitos

- Java JDK 11 ou superior
- Sistema operacional: Windows, Linux ou Mac

---

## Como Compilar

Abra o terminal na pasta do projeto e execute:

```bash
javac Main.java model/*.java view/*.java controller/*.java
```

---

## Como Executar

```bash
java Main
```

---

## Funcionalidades

1. Fazer login / Cadastrar usuário
2. Cadastrar evento
3. Listar todos os eventos
4. Ver próximos eventos
5. Ver eventos acontecendo agora
6. Ver eventos passados
7. Confirmar presença em evento
8. Meus eventos confirmados
9. Cancelar presença
10. Ver participantes de um evento

---

## Tecnologias Utilizadas

- Linguagem: Java
- Paradigma: Orientado a Objetos
- Padrão: MVC (Model-View-Controller)
- Persistência: Arquivo events.data

---

## Autor

Evandro Ferraz - 36456597811@ulife.com.br

