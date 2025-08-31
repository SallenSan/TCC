const API_URL = "http://localhost:8080";

// Função para alternar seções
function mostrarSecao(id) {
  document.querySelectorAll(".secao").forEach(secao => {
    secao.style.display = "none";
  });
  document.getElementById(id).style.display = "block";
}

/* ==================== PACIENTES ==================== */
const formPaciente = document.getElementById("formPaciente");
const listaPacientes = document.getElementById("listaPacientes");

formPaciente.addEventListener("submit", async (e) => {
  e.preventDefault();
  const paciente = {
    nome: document.getElementById("nomePaciente").value,
    email: document.getElementById("emailPaciente").value
  };

  await fetch(`${API_URL}/pacientes`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(paciente)
  });
  carregarPacientes();
  formPaciente.reset();
});

async function carregarPacientes() {
  const res = await fetch(`${API_URL}/pacientes`);
  const pacientes = await res.json();
  listaPacientes.innerHTML = "";
  pacientes.forEach(p => {
    const li = document.createElement("li");
    li.textContent = `ID: ${p.id} - ${p.nome} (${p.email})`;
    listaPacientes.appendChild(li);
  });
}
carregarPacientes();

/* ==================== PSICÓLOGOS ==================== */
const formPsicologo = document.getElementById("formPsicologo");
const listaPsicologos = document.getElementById("listaPsicologos");

formPsicologo.addEventListener("submit", async (e) => {
  e.preventDefault();
  const psicologo = {
    nome: document.getElementById("nomePsicologo").value,
    email: document.getElementById("emailPsicologo").value,
    telefone: document.getElementById("telefonePsicologo").value,
    especialidade: document.getElementById("especialidadePsicologo").value
  };

  await fetch(`${API_URL}/psicologos`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(psicologo)
  });
  carregarPsicologos();
  formPsicologo.reset();
});

async function carregarPsicologos() {
  const res = await fetch(`${API_URL}/psicologos`);
  const psicologos = await res.json();
  listaPsicologos.innerHTML = "";
  psicologos.forEach(p => {
    const li = document.createElement("li");
    li.textContent = `ID: ${p.id} - ${p.nome} (${p.email}) - ${p.telefone} - ${p.especialidade}`;
    listaPsicologos.appendChild(li);
  });
}
carregarPsicologos();

/* ==================== CONSULTAS ==================== */
const formConsulta = document.getElementById("formConsulta");
const listaConsultas = document.getElementById("listaConsultas");

formConsulta.addEventListener("submit", async (e) => {
  e.preventDefault();
  const consulta = {
    dataHora: document.getElementById("dataConsulta").value,
    observacoes: document.getElementById("observacoesConsulta").value,
    paciente: { id: document.getElementById("idPaciente").value },
    psicologo: { id: document.getElementById("idPsicologo").value },
    status: "AGENDADA"
  };

  await fetch(`${API_URL}/consultas`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(consulta)
  });
  carregarConsultas();
  formConsulta.reset();
});

async function carregarConsultas() {
  const res = await fetch(`${API_URL}/consultas`);
  const consultas = await res.json();
  listaConsultas.innerHTML = "";
  consultas.forEach(c => {
    const li = document.createElement("li");
    li.textContent = `ID: ${c.id} - ${c.dataHora} | Paciente: ${c.paciente?.nome} | Psicólogo: ${c.psicologo?.nome} | Status: ${c.status}`;
    listaConsultas.appendChild(li);
  });
}
carregarConsultas();
