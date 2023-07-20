import { Box, useColorModeValue } from '@chakra-ui/react';
import AceEditor from 'react-ace';
import { useResizeDetector } from 'react-resize-detector';

import 'ace-builds/src-noconflict/ext-language_tools';
import './theme-parsec.js';
import './theme-parsec-dark.js';
import 'ace-builds/src-noconflict/theme-nord_dark';
import 'ace-builds/src-noconflict/theme-tomorrow_night_eighties';
import 'ace-builds/src-noconflict/mode-markdown';

export interface EditorProps {
  query: string;
  onQueryChange?: (updatedValue: string) => any;
  readOnly?: boolean;
}

export const Editor = ({ query, onQueryChange, readOnly = false }: EditorProps) => {
  const aceTheme = useColorModeValue('parsec', 'parsec_dark');

  const { width, height, ref } = useResizeDetector();

  return (
    <Box overflow="hidden" flexGrow={1} position="relative" height="100%" ref={ref}>
      <AceEditor
        mode="markdown"
        theme={aceTheme}
        editorProps={{ $blockScrolling: true }}
        value={query}
        onChange={onQueryChange}
        tabSize={2}
        wrapEnabled={true}
        showGutter={true}
        showPrintMargin={false}
        height={`${height}px`}
        width={`${width}px`}
        readOnly={readOnly}
        setOptions={{
          enableBasicAutocompletion: true,
          enableLiveAutocompletion: true,
          enableSnippets: false
        }}
        fontSize="1.125rem"
      />
    </Box>
  );
};
