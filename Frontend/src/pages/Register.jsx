import React, { useState } from 'react';

export default function Register() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    role: 'paciente', // pode ser 'paciente' ou 'psicologo'
  });

  function handleChange(e) {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  }

  function handleSubmit(e) {
    e.preventDefault();
    // Aqui depois faremos a integração com o backend
    console.log('Dados para cadastro:', formData);
    alert('Cadastro enviado (ainda sem backend)');
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-neutral-900 text-white">
      <div className="bg-neutral-800 p-8 rounded-2xl shadow-md w-full max-w-md">
        <h2 className="text-3xl font-semibold text-center mb-6">Cadastro</h2>
        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="block mb-1">Nome</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className="w-full px-4 py-2 rounded bg-neutral-700 text-white focus:outline-none focus:ring-2 focus:ring-yellow-400"
              placeholder="Seu nome completo"
              required
            />
          </div>
          <div>
            <label className="block mb-1">Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className="w-full px-4 py-2 rounded bg-neutral-700 text-white focus:outline-none focus:ring-2 focus:ring-yellow-400"
              placeholder="seuemail@exemplo.com"
              required
            />
          </div>
          <div>
            <label className="block mb-1">Senha</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className="w-full px-4 py-2 rounded bg-neutral-700 text-white focus:outline-none focus:ring-2 focus:ring-yellow-400"
              placeholder="********"
              required
            />
          </div>
          <div>
            <label className="block mb-1">Tipo de usuário</label>
            <select
              name="role"
              value={formData.role}
              onChange={handleChange}
              className="w-full px-4 py-2 rounded bg-neutral-700 text-white focus:outline-none focus:ring-2 focus:ring-yellow-400"
            >
              <option value="paciente">Paciente</option>
              <option value="psicologo">Psicólogo</option>
            </select>
          </div>
          <button
            type="submit"
            className="w-full py-2 px-4 bg-yellow-600 hover:bg-yellow-700 text-white font-bold rounded transition"
          >
            Cadastrar
          </button>
        </form>
      </div>
    </div>
  );
}
