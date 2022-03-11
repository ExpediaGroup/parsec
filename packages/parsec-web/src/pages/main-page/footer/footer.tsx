import { Flex, HStack, Image, Text } from '@chakra-ui/react';

import clojureLogoUrl from '../../../../assets/clojure-logo.svg';

export const Footer = () => {
  return (
    <Flex
      flexShrink={0}
      p="0 2rem"
      align="center"
      justify="space-between"
      height="4.7rem"
      color="white"
      textStyle="parsec"
      _light={{
        bg: 'parsec.pink'
      }}
      _dark={{
        bg: 'parsec.violet'
      }}
    >
      <HStack spacing="1rem">
        <Image src={clojureLogoUrl} alt="Clojure" width="36px" />
        <Text fontSize="xl" fontWeight="bold" textTransform="lowercase">
          Powered by Clojure
        </Text>
      </HStack>
    </Flex>
  );
};
