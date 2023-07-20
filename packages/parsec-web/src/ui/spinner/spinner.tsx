import { Spinner as ChakraSpinner } from '@chakra-ui/react';
import type { SpinnerProps as ChakraSpinnerProps } from '@chakra-ui/react';

export type SpinnerProps = Partial<ChakraSpinnerProps>;

/**
 * Spinner component to indicate loading state.
 */
export const Spinner = (props: SpinnerProps) => {
  return <ChakraSpinner thickness="3px" speed="0.65s" emptyColor="gray.200" color="parsec.pink" size="xl" {...props} />;
};
