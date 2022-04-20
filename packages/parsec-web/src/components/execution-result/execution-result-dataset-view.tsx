import {
  Heading,
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  TableContainer,
  useDisclosure,
  Collapse,
  HStack,
  IconButton,
  useColorModeValue
} from '@chakra-ui/react';
import { useMemo } from 'react';
import type { Cell } from 'react-table';
import { useTable } from 'react-table';

import { Card } from '../../card/card';
import { iconFactoryAs } from '../../shared/icon-factory';
import type { ExecutionResultDataSet } from '../../types/parsec';

import { CellRenderer } from './cell-renderer';

interface Props {
  dataSet: ExecutionResultDataSet;
}

export const ExecutionResultDataSetView = ({ dataSet }: Props) => {
  const { isOpen, onToggle } = useDisclosure({ defaultIsOpen: true });

  const data = dataSet.data;
  const columns = useMemo(
    () =>
      dataSet.columns.map((column) => ({
        Header: column,
        accessor: column,
        Cell: (cell: Cell) => CellRenderer(cell.value)
      })),
    [dataSet.columns]
  );

  const { getTableProps, getTableBodyProps, headers, rows, prepareRow } = useTable({
    columns,
    data
  });

  return (
    <Card p="1rem" bg={useColorModeValue('parsec.grey.900', 'parsec.raisin')}>
      <HStack align="center" spacing="1rem" justify="space-between">
        <Heading as="h2">{dataSet.name}</Heading>

        <IconButton
          aria-label="Expand/collapse"
          icon={isOpen ? iconFactoryAs('chevronUp') : iconFactoryAs('chevronDown')}
          size="sm"
          onClick={onToggle}
          title={isOpen ? 'Collapse this data set' : 'Expand this data set'}
        />
      </HStack>

      <Collapse in={isOpen} animateOpacity>
        <TableContainer>
          <Table {...getTableProps()}>
            <Thead>
              <Tr>
                {headers.map((header) => (
                  <Th {...header.getHeaderProps()} color="parsec.pink" fontSize="sm">
                    {header.render('Header')}
                  </Th>
                ))}
              </Tr>
            </Thead>
            <Tbody {...getTableBodyProps()}>
              {rows.map((row) => {
                prepareRow(row);
                return (
                  <Tr {...row.getRowProps()}>
                    {row.cells.map((cell) => {
                      return <Td {...cell.getCellProps()}>{cell.render('Cell')}</Td>;
                    })}
                  </Tr>
                );
              })}
            </Tbody>
          </Table>
        </TableContainer>
      </Collapse>
    </Card>
  );
};
