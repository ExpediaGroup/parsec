import { useColorModeValue } from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import AceEditor from 'react-ace';
import { useDebouncedCallback } from 'use-debounce';

import 'ace-builds/src-noconflict/ext-language_tools';
import './theme-parsec.js';
import 'ace-builds/src-noconflict/theme-nord_dark';
import 'ace-builds/src-noconflict/mode-markdown';

interface Props {
  contents: string;
  onContentsChange?: (updatedValue: string) => any;
  readOnly?: boolean;
}

export const Editor = ({ contents, onContentsChange, readOnly = false }: Props) => {
  const aceTheme = useColorModeValue('parsec', 'nord_dark');

  const [internalValue, setInternalValue] = useState(contents);

  // Overwrite internal state if external contents change
  useEffect(() => {
    setInternalValue(contents);
  }, [contents]);

  // Debounce value changes to avoid too-frequent updates
  const debounced = useDebouncedCallback((value) => {
    setInternalValue(value);

    if (onContentsChange) {
      onContentsChange(internalValue);
    }
  }, 25);

  return (
    <AceEditor
      mode="markdown"
      theme={aceTheme}
      editorProps={{ $blockScrolling: true }}
      value={internalValue}
      onChange={debounced}
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
        enableSnippets: true
      }}
      fontSize="1.125rem"
    />
  );
};
