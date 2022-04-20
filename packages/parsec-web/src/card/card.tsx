import type { FlexProps } from '@chakra-ui/react';
import { Flex, useColorModeValue } from '@chakra-ui/react';

export const Card = ({ children, ...flexProps }: FlexProps) => {
  return (
    <Flex
      flexDirection="column"
      align="stretch"
      {...flexProps}
      bg={useColorModeValue('white', 'gray.700')}
      //borderColor={useColorModeValue('gray.300', 'synthwave.50')}
      //borderWidth="1px"
      borderRadius="0.5rem"
      overflow="hidden"
      p="0.5rem"
      {...flexProps}
    >
      {children}
    </Flex>
  );
};
