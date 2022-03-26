import { Box, Heading, VStack } from '@chakra-ui/react';

import { parsecTheme } from '../../theme';

export const ThemePage = () => {
  const colors: Record<string, string> = parsecTheme.colors.parsec;
  console.log(colors);
  return (
    <VStack spacing="1rem" direction="column" align="stretch" flexGrow={1} p="2rem">
      <Heading>Theme</Heading>

      {Object.keys(parsecTheme.colors.parsec).map((color: string) => (
        <Box bg={`parsec.${color}`} p="1rem" borderRadius="lg">
          parsec.{color}
        </Box>
      ))}
      {Object.keys(parsecTheme.colors.synthwave).map((color: string) => (
        <Box bg={`synthwave.${color}`} p="1rem" borderRadius="lg">
          synthwave.{color}
        </Box>
      ))}
    </VStack>
  );
};
