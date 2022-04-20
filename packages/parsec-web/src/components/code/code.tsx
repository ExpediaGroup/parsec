import type { BoxProps } from '@chakra-ui/react';
import { useColorModeValue } from '@chakra-ui/react';
import { Box } from '@chakra-ui/react';
import { memo } from 'react';
import { PrismAsyncLight as SyntaxHighlighter } from 'react-syntax-highlighter';
import { ghcolors, synthwave84, tomorrow } from 'react-syntax-highlighter/dist/esm/styles/prism';

import darkStyle from './style-parsec-dark.js';
import lightStyle from './style-parsec.js';

export interface CodeProps {
  children?: string;
  language: string;
  showLineNumbers?: boolean;
  theme?: string;
}

export const Code = memo(({ children, language, showLineNumbers, theme, ...boxProps }: CodeProps & BoxProps) => {
  const defaultStyle = useColorModeValue(lightStyle, darkStyle);
  return (
    <Box position="relative" fontSize={{ base: 'sm', md: 'md' }} {...boxProps}>
      <SyntaxHighlighter
        language={language}
        style={theme ?? defaultStyle}
        showLineNumbers={showLineNumbers ?? true}
        lineNumberStyle={{
          color: '#666666'
        }}
        customStyle={{
          margin: 0
        }}
      >
        {children ?? ''}
      </SyntaxHighlighter>
    </Box>
  );
});
