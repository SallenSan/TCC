const express = require("express");
const path = require("path");

const app = express();
const PORT = 3000; // frontend vai rodar aqui

// Middleware para servir os arquivos da pasta "public"
app.use(express.static(path.join(__dirname, "public")));

// Rota principal -> carrega index.html
app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "public", "index.html"));
});

// Inicia o servidor
app.listen(PORT, () => {
  console.log(`Frontend rodando em http://localhost:${PORT}`);
});
