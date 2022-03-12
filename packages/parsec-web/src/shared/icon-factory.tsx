import { Icon } from '@chakra-ui/react';
import type { ReactElement } from 'react';
import type { IconType } from 'react-icons';
import { AiOutlineMenu } from 'react-icons/ai';
import { BsFillMoonStarsFill, BsFillSunFill } from 'react-icons/bs';
import { GoMarkGithub, GoX } from 'react-icons/go';
import { GrStatusUnknown } from 'react-icons/gr';

const icons = {
  close: GoX,
  github: GoMarkGithub,
  darkMode: BsFillMoonStarsFill,
  lightMode: BsFillSunFill,
  menu: AiOutlineMenu,
  unknown: GrStatusUnknown
};

type IconFactory = typeof icons;
type IconFactoryKeys = keyof IconFactory;

export const iconFactory = (key: IconFactoryKeys): IconType => {
  if (icons[key]) {
    return icons[key];
  }

  // Fallback!
  console.error(`[ICON_FACTORY] Missing icon for ${key}!`);
  return GrStatusUnknown;
};

export const iconFactoryAs = (key: IconFactoryKeys, props = {}): ReactElement => {
  return <Icon as={iconFactory(key)} {...props} />;
};
