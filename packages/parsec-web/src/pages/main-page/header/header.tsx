import {
  Flex,
  Heading,
  Icon,
  IconButton,
  Image,
  Menu,
  MenuButton,
  MenuItem,
  MenuList,
  Stack,
  useColorMode,
  useColorModeValue,
  useDisclosure
} from '@chakra-ui/react';

import parsecLogoUrl from '../../../../assets/parsec-logo.svg';
import { Link } from '../../../components/link/link';
import { iconFactory, iconFactoryAs } from '../../../shared/icon-factory';

import { Stripes } from './stripes';

export const Header = () => {
  const { isOpen, onToggle } = useDisclosure();
  const { colorMode, toggleColorMode } = useColorMode();
  const hoverColor = useColorModeValue('parsec.purple', 'parsec.pink');

  const headerItemProps: any = {
    fontFamily: 'heading',
    fontSize: '2rem',
    fontWeight: '600',
    px: '1rem',
    display: 'flex',
    flexDirection: 'column',
    position: 'relative',
    justifyContent: 'center',
    borderTop: '4px solid transparent',
    borderBottom: '4px solid transparent',
    _hover: {
      borderBottom: '4px solid',
      color: hoverColor
    }
  };

  return (
    <Flex
      flexShrink={0}
      p="0 2rem"
      align={{ base: 'center', md: 'stretch' }}
      justify="space-between"
      height={{ base: 'unset', md: '4.7rem' }}
      color="white"
      wrap={{ base: 'wrap', md: 'unset' }}
      _light={{
        bg: 'parsec.pink'
      }}
      _dark={{
        bg: 'parsec.violet'
      }}
    >
      <Flex
        as={Link}
        to="/"
        subtle={true}
        align="center"
        cursor="pointer"
        my="0.5rem"
        /*onClick="#(re-frame/dispatch [::events/navigate :home])"*/
      >
        <Image src={parsecLogoUrl} boxSize="3rem" mr="1rem" />

        <Flex align="baseline">
          <Heading fontSize="3rem" fontWeight="400">
            parsec
          </Heading>
          <Heading fontSize="1.25rem" fontWeight="400">
            beta
          </Heading>
        </Flex>
      </Flex>

      <Icon
        as={iconFactory(isOpen ? 'close' : 'menu')}
        display={{ base: 'flex', md: 'none' }}
        boxSize="1.5rem"
        onClick={onToggle}
        focusable={true}
      />

      <Stack
        display={{
          base: isOpen ? 'flex' : 'none',
          md: 'flex'
        }}
        direction={{ base: 'column', md: 'row' }}
        width={{ base: 'full', md: 'auto' }}
        spacing={{ base: '1.5rem', md: 0 }}
        mb={{ base: '1rem', md: 0 }}
        align="stretch"
        justifyContent="end"
        textAlign="center"
      >
        <Link to="/" subtle={true} {...headerItemProps}>
          home
        </Link>
        <Link to="/editor" subtle={true} {...headerItemProps}>
          editor
        </Link>

        <Menu>
          <MenuButton {...headerItemProps} sx={{ '>span': { flex: 'unset' } }}>
            learn
          </MenuButton>
          <MenuList
            maxW={{ base: '100%', md: 'auto' }}
            _light={{
              color: 'parsec.violet'
            }}
          >
            <Link to="/quickstart" subtle={true}>
              <MenuItem fontFamily="heading" fontSize="2rem">
                quick start
              </MenuItem>
            </Link>
            <Link to="/reference" subtle={true}>
              <MenuItem fontFamily="heading" fontSize="2rem">
                reference
              </MenuItem>
            </Link>
          </MenuList>
        </Menu>

        <Stripes display={{ base: 'none', md: 'flex' }} />

        <Flex flexDirection="column" justifyContent="center">
          <Link href="https://github.com/ExpediaGroup/parsec" subtle={true} height="2.25rem">
            <Icon as={iconFactory('github')} mx="1rem" boxSize="2.25rem" _hover={{ color: hoverColor }} />
          </Link>
        </Flex>

        <Flex flexDirection="column" justifyContent="center" ml="1rem">
          <IconButton
            icon={iconFactoryAs(`${colorMode == 'light' ? 'dark' : 'light'}Mode`)}
            onClick={toggleColorMode}
            aria-label="Toggle color mode"
            height={{ base: '56px', md: '2.25rem' }}
            variant="ghost"
            _hover={{
              bg: 'transparent',
              color: hoverColor
            }}
          />
        </Flex>
      </Stack>
    </Flex>
  );
};
