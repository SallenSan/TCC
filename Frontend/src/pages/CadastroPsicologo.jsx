import React, { useState } from 'react';
import axios from 'axios';

const CadastroPsicologo = () => {
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    senha: '',
    telefone: '',
    especialidade: '',
    crp: ''
  });

  const [mensagem, setMensagem] = useState('');
  const [erro, setErro] = useState('');

  const validarEmail = (email) => {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  };

  const handleChange = (e) => {
    let { name, value } = e.target;

    if (name === 'telefone') {
      value = value.replace(/\D/g, '');
      if (value.length > 10) {
        value = value.replace(/^(\d{2})(\d{5})(\d{4})$/, '($1) $2-$3');
      } else {
        value = value.replace(/^(\d{2})(\d{4})(\d{4})$/, '($1) $2-$3');
      }
    }

    setFormData({
      ...formData,
      [name]: value
    });

    setErro('');
    setMensagem('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.nome || !formData.email || !formData.senha || !formData.telefone || !formData.especialidade || !formData.crp) {
      setErro('Todos os campos são obrigatórios.');
      return;
    }

    if (!validarEmail(formData.email)) {
      setErro('E-mail inválido.');
      return;
    }

    if (formData.senha.length < 6) {
      setErro('A senha deve ter no mínimo 6 caracteres.');
      return;
    }

    try {
      await axios.post('http://localhost:8080/api/psicologos', formData); // <- URL corrigida
      setMensagem('Cadastro realizado com sucesso!');
      setFormData({ nome: '', email: '', senha: '', telefone: '', especialidade: '', crp: '' });
    } catch (error) {
      if (error.response && error.response.data) {
        setErro(error.response.data.message || 'Erro ao cadastrar. Verifique os dados.');
      } else {
        setErro('Erro de conexão com o servidor.');
      }
    }
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-zinc-900 text-white">
      <form onSubmit={handleSubmit} className="bg-zinc-800 p-8 rounded-lg shadow-lg w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6 text-center">Cadastro de Psicólogo</h2>

        {mensagem && <p className="mb-4 text-green-400 text-sm text-center">{mensagem}</p>}
        {erro && <p className="mb-4 text-red-500 text-sm text-center">{erro}</p>}

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
          placeholder="Senha (mín. 6 caracteres)"
          className="w-full mb-4 p-2 rounded bg-zinc-700 placeholder-gray-400"
          required
        />

        <input
          type="text"
          name="telefone"
          value={formData.telefone}
          onChange={handleChange}
          placeholder="Telefone"
          className="w-full mb-4 p-2 rounded bg-zinc-700 placeholder-gray-400"
          required
        />

        <input
          type="text"
          name="especialidade"
          value={formData.especialidade}
          onChange={handleChange}
          placeholder="Especialidade"
          className="w-full mb-4 p-2 rounded bg-zinc-700 placeholder-gray-400"
          required
        />

        <input
          type="text"
          name="crp"
          value={formData.crp}
          onChange={handleChange}
          placeholder="CRP (Ex: 12345/XX)"
          className="w-full mb-6 p-2 rounded bg-zinc-700 placeholder-gray-400"
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
