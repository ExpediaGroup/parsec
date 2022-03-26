import { extendTheme } from '@chakra-ui/react';
import { createBreakpoints } from '@chakra-ui/theme-tools';

const breakpoints = createBreakpoints({
  sm: '30rem',
  md: '48rem',
  lg: '62rem',
  xl: '75rem',
  xxl: '100rem'
});

export const parsecTheme = extendTheme({
  useSystemColorMode: false,
  initialColorMode: 'dark',
  breakpoints,
  colors: {
    parsec: {
      blue: '#2de2e6',
      fuchsia: '#f6019d',
      orange: '#ff6c11',
      pink: '#fd3777',
      violet: '#2e2157',
      purple: '#791e94',
      yellow: '#f9c80e'
    },
    synthwave: {
      50: '#0d0221',
      100: '#241734',
      200: '#261447',
      300: '#2e2157',
      400: '#541388',
      500: '#ff3864',
      //600: '#2de2e6',
      700: '#ff6c11',
      800: '#fd3777',
      900: '#f706cf',
      A00: '#fd1d53',
      //A10: '#f9c80e',
      A20: '#ff4365',
      A30: '#f6019d',
      A40: '#650d89',
      A60: '#791e94'
    }
  },
  fonts: {
    heading: "'Megrim', sans-serif",
    body: "'Open Sans', sans-serif"
  },
  fontSizes: {
    xs: '0.75rem',
    sm: '0.875rem',
    md: '1.0rem',
    lg: '1.125rem',
    xl: '1.25rem',
    '2xl': '1.5rem',
    '3xl': '1.7rem',
    '4xl': '1.95rem',
    '5xl': '2.25rem',
    '6xl': '2.6rem'
  },
  styles: {
    global: {
      'html, body': {
        fontSize: 'md'
      }
    }
  },
  textStyles: {
    parsec: {
      fontFamily: "'Megrim', sans-serif"
    }
  },
  components: {
    Button: {
      variants: {
        fancy: {
          fontFamily: "'Megrim', sans-serif",
          fontSize: 'xl',
          padding: '1.5rem 2.5rem',
          _dark: {
            //bgGradient: 'linear(to top, parsec.violet 0%, parsec.purple 100%)',
            border: '4px solid',
            borderColor: 'parsec.blue',
            color: 'white'
          },
          _light: {
            border: '4px solid',
            borderColor: 'parsec.blue'
          },

          _hover: {
            _dark: {
              boxShadow: 'lg',
              borderColor: 'parsec.yellow'
            },
            _light: {
              boxShadow: 'lg',
              borderColor: 'parsec.yellow'
            }
          },
          _focus: {
            _dark: {
              boxShadow: '0 0 0 3px #fd3777'
            },
            _light: {
              boxShadow: '0 0 0 3px #791e94'
            }
          }
        },
        ghost: {
          _focus: {
            _dark: {
              boxShadow: '0 0 0 3px #fd3777'
            },
            _light: {
              boxShadow: '0 0 0 3px #791e94'
            }
          }
        }
      }
    }
  }
});
