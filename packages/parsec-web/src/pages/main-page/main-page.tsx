import { Route, Routes } from 'react-router-dom';

import { EditorPage } from '../editor-page/editor-page';
import { HomePage } from '../home-page/home-page';
import { ReferencePage } from '../reference-page/reference-page';

import { Footer } from './footer/footer';
import { Header } from './header/header';

export const MainPage = () => {
  return (
    <>
      <Header />

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/editor" element={<EditorPage />} />
        <Route path="/reference" element={<ReferencePage />} />
      </Routes>

      <Footer />
    </>
  );
};
