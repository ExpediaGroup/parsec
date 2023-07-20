import type { FlexProps } from '@chakra-ui/react';
import { Flex } from '@chakra-ui/react';

export interface SectionProps {
  innerProps?: FlexProps;
  outerProps?: FlexProps;
  children?: JSX.Element;
}

/**
 * Home page section component.
 */
export const Section = ({ children, innerProps, outerProps }: SectionProps) => (
  <Flex direction="row" minHeight="80vh" align="center" justify="center" {...outerProps}>
    <Flex direction="column" align="stretch" flexGrow={1} {...innerProps}>
      {children}
    </Flex>
  </Flex>
);
