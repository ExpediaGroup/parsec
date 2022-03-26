import { useColorModeValue } from '@chakra-ui/react';
import AceEditor from 'react-ace';

import 'ace-builds/src-noconflict/ext-language_tools';
import './theme-parsec.js';
import './theme-parsec-dark.js';
import 'ace-builds/src-noconflict/theme-nord_dark';
import 'ace-builds/src-noconflict/theme-tomorrow_night_eighties';
import 'ace-builds/src-noconflict/mode-markdown';

interface Props {
  query: string;
  onQueryChange?: (updatedValue: string) => any;
  readOnly?: boolean;
}

export const Editor = ({ query, onQueryChange, readOnly = false }: Props) => {
  const aceTheme = useColorModeValue('parsec', 'tomorrow_night_eighties');

  return (
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
      width="100%"
      minLines={30}
      maxLines={Number.POSITIVE_INFINITY}
      readOnly={readOnly}
      setOptions={{
        enableBasicAutocompletion: true,
        enableLiveAutocompletion: true,
        enableSnippets: false
      }}
      fontSize="1.125rem"
    />
  );
};
