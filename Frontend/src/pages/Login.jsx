import React from 'react';
import { Link } from 'react-router-dom';

export default function Login() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-neutral-900 text-white">
      <div className="bg-neutral-800 p-8 rounded-2xl shadow-md w-full max-w-md">
        <h2 className="text-3xl font-semibold text-center mb-6">Login</h2>
        <form className="space-y-4">
          <div>
            <label className="block mb-1">Email</label>
            <input
              type="email"
              className="w-full px-4 py-2 rounded bg-neutral-700 text-white focus:outline-none focus:ring-2 focus:ring-yellow-400"
              placeholder="seuemail@exemplo.com"
            />
          </div>
          <div>
            <label className="block mb-1">Senha</label>
            <input
              type="password"
              className="w-full px-4 py-2 rounded bg-neutral-700 text-white focus:outline-none focus:ring-2 focus:ring-yellow-400"
              placeholder="********"
            />
          </div>
          <button
            type="submit"
            className="w-full py-2 px-4 bg-yellow-600 hover:bg-yellow-700 text-white font-bold rounded transition"
          >
            Entrar
          </button>
        </form>
        <p className="mt-4 text-center">
          Não tem uma conta?{' '}
          <Link to="/register" className="text-yellow-400 hover:underline">
            Cadastre-se
          </Link>
        </p>
      </div>
    </div>
  );
}
