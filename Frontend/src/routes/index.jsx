import { BrowserRouter, Routes, Route } from "react-router-dom";
import CadastroPsicologo from "../pages/CadastroPsicologo";

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/cadastro-psicologo" element={<CadastroPsicologo />} />
        {/* Adicione outras rotas aqui */}
      </Routes>
    </BrowserRouter>
  );
}
