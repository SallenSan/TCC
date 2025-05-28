import React, { useState } from 'react';
import axios from 'axios';

const CadastroPsicologo = () => {
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    senha: '',
    crp: ''
  });

  const [mensagem, setMensagem] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/psicologos', formData);
      setMensagem('Cadastro realizado com sucesso!');
      setFormData({ nome: '', email: '', senha: '', crp: '' });
    } catch (error) {
      setMensagem('Erro ao cadastrar psicólogo. Verifique os dados.');
    }
  };

  return (
    <div className="flex justify-center items-center h-screen bg-zinc-900 text-white">
      <form onSubmit={handleSubmit} className="bg-zinc-800 p-8 rounded-lg shadow-lg w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6 text-center">Cadastro de Psicólogo</h2>

        {mensagem && <p className="mb-4 text-sm text-center">{mensagem}</p>}

        <input
          type="text"
          name="nome"
          value={formData.nome}
          onChange={handleChange}
          placeholder="Nome"
          className="w-full mb-4 p-2 rounded bg-zinc-700 placeholder-gray-400"
          required
        />

        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          placeholder="E-mail"
          className="w-full mb-4 p-2 rounded bg-zinc-700 placeholder-gray-400"
          required
        />

        <input
          type="password"
          name="senha"
          value={formData.senha}
          onChange={handleChange}
          placeholder="Senha"
          className="w-full mb-4 p-2 rounded bg-zinc-700 placeholder-gray-400"
          required
        />

        <input
          type="text"
          name="crp"
          value={formData.crp}
          onChange={handleChange}
          placeholder="CRP"
          className="w-full mb-4 p-2 rounded bg-zinc-700 placeholder-gray-400"
          required
        />

        <button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-700 py-2 px-4 rounded">
          Cadastrar
        </button>
      </form>
    </div>
  );
};

export default CadastroPsicologo;
