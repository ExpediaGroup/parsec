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
  useColorMode,
  useColorModeValue
} from '@chakra-ui/react';

import parsecLogoUrl from '../../../../assets/parsec-logo.svg';
import { Link } from '../../../components/link/link';
import { iconFactory, iconFactoryAs } from '../../../shared/icon-factory';

import { Stripes } from './stripes';

export const Header = () => {
  const { colorMode, toggleColorMode } = useColorMode();
  const hoverColor = useColorModeValue('synthwave.A60', 'synthwave.800');

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
      p="0 2rem"
      align="stretch"
      justify="space-between"
      height="4.7rem"
      color="white"
      _light={{
        bg: 'synthwave.800'
      }}
      _dark={{
        bg: 'synthwave.300'
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

      <Flex align="stretch">
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
            _light={{
              color: 'synthwave.200'
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

        <Stripes />

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
            variant="ghost"
            _hover={{
              bg: 'transparent',
              color: hoverColor
            }}
          />
        </Flex>
      </Flex>
    </Flex>
  );
};
