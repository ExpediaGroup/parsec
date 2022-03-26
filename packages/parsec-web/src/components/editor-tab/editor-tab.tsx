import {
  Button,
  ButtonGroup,
  Editable,
  EditableInput,
  EditablePreview,
  Heading,
  HStack,
  IconButton,
  MenuItem,
  MenuOptionGroup,
  Stack,
  VStack
} from '@chakra-ui/react';

import { iconFactory } from '../../shared/icon-factory';
import type { TabState } from '../../store/editor.slice';
import { useExecuteQueryMutation, useValidateQueryMutation } from '../../store/parsec.slice';
import { Editor } from '../editor/editor';
import { IconButtonMenu } from '../icon-button-menu/icon-button-menu';

interface Props {
  tab: TabState;
  query: string;
  onLabelChange?: (label: string) => void;
  onQueryChange?: (query: string) => void;
  readOnly?: boolean;
}

export const EditorTab = ({ tab, query, onLabelChange, onQueryChange, readOnly = false }: Props) => {
  const [executeQuery, { isLoading }] = useExecuteQueryMutation();
  const [validateQuery] = useValidateQueryMutation();

  return (
    <VStack align="stretch">
      <Stack direction={{ base: 'column', md: 'row' }} align="flex-start" justify="space-between">
        <Editable defaultValue={tab.label} onChange={onLabelChange} mb="2rem">
          <EditablePreview as={Heading} />
          <EditableInput />
        </Editable>

        <HStack>
          <ButtonGroup>
            <Button bg="parsec.violet" onClick={() => executeQuery({ query })}>
              Execute
            </Button>
            <IconButtonMenu icon={iconFactory('menu')} aria-label="Additional options" tooltip="Additional options">
              <MenuItem onClick={() => validateQuery({ query })}>Validate</MenuItem>
            </IconButtonMenu>
          </ButtonGroup>
        </HStack>
      </Stack>

      <Editor query={query} onQueryChange={onQueryChange} />
    </VStack>
  );
};
