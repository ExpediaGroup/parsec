import { ColorModeScript, ChakraProvider } from '@chakra-ui/react';
import React from 'react';
import ReactDOM from 'react-dom';
import { Provider as Redux } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { PersistGate } from 'redux-persist/integration/react';

import { MainPage } from './pages/main-page/main-page';
import { store, persistor } from './store/store';
import { parsecTheme } from './theme';

import '@fontsource/megrim';
import '@fontsource/open-sans';

import './index.css';

const App = () => {
  return (
    <>
      <ColorModeScript initialColorMode={parsecTheme.config.initialColorMode} />
      <ChakraProvider resetCSS theme={parsecTheme} portalZIndex={10}>
        <Redux store={store}>
          <PersistGate loading={undefined} persistor={persistor}>
            <BrowserRouter>
              <MainPage />
            </BrowserRouter>
          </PersistGate>
        </Redux>
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
