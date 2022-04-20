import type { Enable } from 're-resizable';

export const enableResizable = (options: Enable) => {
  return {
    top: false,
    right: false,
    bottom: false,
    left: false,
    topRight: false,
    bottomRight: false,
    bottomLeft: false,
    topLeft: false,

    ...options
  };
};
