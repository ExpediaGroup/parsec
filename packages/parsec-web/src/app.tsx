import { ColorModeScript, ChakraProvider } from '@chakra-ui/react';
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';

import { MainPage } from './pages/main-page/main-page';
import { parsecTheme } from './theme';

import '@fontsource/megrim';
import '@fontsource/open-sans';

import './index.css';

const App = () => {
  return (
    <>
      <ColorModeScript initialColorMode={parsecTheme.config.initialColorMode} />
      <ChakraProvider resetCSS theme={parsecTheme} portalZIndex={10}>
        <BrowserRouter>
          <MainPage />
        </BrowserRouter>
      </ChakraProvider>
    </>
  );
};

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.querySelector('#root')
);
