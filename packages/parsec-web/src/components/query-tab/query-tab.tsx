import type { FlexProps } from '@chakra-ui/react';
import {
  Button,
  ButtonGroup,
  Editable,
  EditableInput,
  EditablePreview,
  Heading,
  HStack,
  Icon,
  MenuItem,
  Stack,
  Text,
  VStack
} from '@chakra-ui/react';
import { Resizable } from 're-resizable';
import { useEffect, useMemo } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { iconFactory, iconFactoryAs } from '../../shared/icon-factory';
import { enableResizable } from '../../shared/resizable-utils';
import type { QueryTabState } from '../../store/editor.slice';
import { EditorLayout } from '../../store/editor.slice';
import { editorSlice } from '../../store/editor.slice';
import { useExecuteQueryMutation, useValidateQueryMutation } from '../../store/parsec.slice';
import type { RootState } from '../../store/store';
import { IconButtonMenu } from '../../ui/icon-button-menu/icon-button-menu';
import { Editor } from '../editor/editor';
import { ExecutionResult } from '../execution-result/execution-result';
import { IconButton } from '../../ui/icon-button/icon-button';

export type QueryTabProps = {
  tab: QueryTabState;
  query: string;
  onLabelChange?: (label: string) => void;
  onRunningChange?: (isLoading: boolean) => void;
  onQueryChange?: (query: string) => void;
  readOnly?: boolean;
} & Omit<FlexProps, 'direction'>;

export const QueryTab = ({
  tab,
  query,
  onLabelChange,
  onQueryChange,
  onRunningChange,
  readOnly = false,
  ...flexProps
}: QueryTabProps) => {
  const dispatch = useDispatch();
  const { layout } = useSelector((state: RootState) => state.editor);

  const [executeQuery, { data: executeData, isLoading: isExecuteLoading }] = useExecuteQueryMutation();
  const [validateQuery, { data: validateData, isLoading: isValidationLoading, reset: resetValidate }] = useValidateQueryMutation();

  // If either the execute or validate functions are being called, we're loading!
  const isLoading = isExecuteLoading || isValidationLoading;

  const toggleLayout = () => {
    dispatch(editorSlice.actions.toggleLayout());
  };

  useEffect(() => {
    if (onRunningChange && tab.isRunning !== isLoading) {
      onRunningChange(isLoading);
    }
  }, [isLoading, onRunningChange]);

  const isValid = validateData === undefined || validateData?.errors.length === 0;

  // If we have invalid validation data, use that, otherwise use execution data
  const executionResult = isValid === false ? validateData : executeData;

  const validateDOM = useMemo(() => {
    if (!validateData) {
      return null;
    }

    return (
      <HStack color={isValid ? "green.500" : "red.500"} align="center">
        {(isValid 
          ? (<><Text>Query is valid</Text><Icon as={iconFactory('valid')}/></>)
          : (<><Text>Query is invalid</Text><Icon as={iconFactory('invalid')}/></>))}
      </HStack>
    );
    
  }, [validateData])

  const onExecute = () => {
    resetValidate();
    executeQuery({ query });
  };

  const onQueryChangeInternal = (query: string) => {
    if (onQueryChange) {
      onQueryChange(query);
    }
    resetValidate();
  }

  return (
    <VStack align="stretch" overflow="hidden" {...flexProps}>
      <Stack direction={{ base: 'column', md: 'row' }} align="flex-start" justify="space-between" p="0.5rem">
        <Editable defaultValue={tab.label} onChange={onLabelChange} mb="2rem">
          <EditablePreview as={Heading} />
          <EditableInput />
        </Editable>

        <HStack>
          {validateDOM}
          <ButtonGroup>
            <Button
              variant="primary"
              leftIcon={iconFactoryAs('execute')}
              isLoading={isExecuteLoading}
              onClick={onExecute}
            >
              Execute
            </Button>
            <IconButtonMenu icon="menu" aria-label="Additional options" tooltip="Additional options">
              <MenuItem onClick={() => validateQuery({ query })}>Validate</MenuItem>
            </IconButtonMenu>

            <IconButton
              icon={layout}
              onClick={toggleLayout}
              aria-label="Toggle layout"
              _hover={{
                bg: 'transparent',
                color: 'parsec.pink'
              }}
            />
          </ButtonGroup>
        </HStack>
      </Stack>

      <Stack
        direction={layout === EditorLayout.DockBottom ? 'column' : 'row'}
        flexGrow={1}
        align="stretch"
        spacing={0}
        overflow="hidden"
      >
        <Editor query={query} onQueryChange={onQueryChangeInternal} />

        {/* Using two different components to isolate resize state when switching */}
        {layout === EditorLayout.DockBottom && (
          <ExecutionResult
            as={Resizable}
            defaultSize={{
              width: '100%',
              height: '50%'
            }}
            minHeight="20%"
            maxHeight="80%"
            enable={enableResizable({
              top: true
            })}
            borderTop="2px solid"
            borderColor="parsec.pink"
            flexGrow={1}
            overflow="auto"
            results={executionResult}
          />
        )}

        {layout === EditorLayout.DockRight && (
          <ExecutionResult
            as={Resizable}
            defaultSize={{
              width: '40%',
              height: 'inherit'
            }}
            minWidth="20%"
            maxWidth="80%"
            enable={enableResizable({
              left: true
            })}
            borderLeft="2px solid"
            borderColor="parsec.pink"
            overflow="auto"
            results={executionResult}
          />
        )}
      </Stack>
    </VStack>
  );
};
