import type { FlexProps } from '@chakra-ui/react';
import { Card as ChakraCard, CardBody, Flex, useColorModeValue } from '@chakra-ui/react';

export type CardProps = FlexProps;

/**
 * Card component.
 */
export const Card = ({ children, ...flexProps }: CardProps) => {
  return (
    <ChakraCard
      flexDirection="column"
      align="stretch"
      overflow="hidden"
      p="0.5rem"
      {...flexProps}
    >
      {children}
    </ChakraCard>
  );
};
