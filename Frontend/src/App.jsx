import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CadastroPsicologo from './pages/CadastroPsicologo';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/cadastro-psicologo" element={<CadastroPsicologo />} />
        {/* Outras rotas... */}
      </Routes>
    </Router>
  );
}

export default App;
