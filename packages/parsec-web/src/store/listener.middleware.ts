import { createListenerMiddleware } from '@reduxjs/toolkit';

import { addAppListeners } from './app.slice';
import type { RootState } from './store';

export const listenerMiddleware = createListenerMiddleware<RootState>();

export type StartListening = typeof listenerMiddleware.startListening;

// Register listeners for all slices here
addAppListeners(listenerMiddleware.startListening);
