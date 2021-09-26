import { Box } from '@chakra-ui/react';

export const Stripes = () => {
  return (
    <Box overflow="hidden">
      <Box transform="skewY(-45deg)" position="relative" px="1rem">
        <Box backgroundColor="synthwave.A10" height="2rem" width="10rem" position="relative" left="-1rem" />
        <Box backgroundColor="synthwave.A30" height="2rem" width="9rem" position="relative" left="1rem" />
        <Box backgroundColor="synthwave.600" height="2rem" width="8rem" position="relative" left="3rem" />
      </Box>
    </Box>
  );
};
