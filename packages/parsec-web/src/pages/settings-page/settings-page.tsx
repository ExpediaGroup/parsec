import { Heading } from '@chakra-ui/react';

import { ThemePage } from '../theme-page/theme-page';

export const SettingsPage = () => {
  return (
    <>
      <Heading as="h2" textStyle="parsec">
        Settings
      </Heading>
      <ThemePage />
    </>
  );
};
