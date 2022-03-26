import { Icon } from '@chakra-ui/react';
import type { ReactElement } from 'react';
import type { IconType } from 'react-icons';
import { AiFillEdit, AiOutlineMenu } from 'react-icons/ai';
import { BsFillMoonStarsFill, BsFillSunFill } from 'react-icons/bs';
import { GoChevronDown, GoChevronLeft, GoChevronRight, GoChevronUp, GoMarkGithub, GoX } from 'react-icons/go';
import { GrStatusUnknown } from 'react-icons/gr';
import { MdAdd } from 'react-icons/md';
import { VscListTree, VscSettingsGear } from 'react-icons/vsc';

const icons = {
  chevronDown: GoChevronDown,
  chevronLeft: GoChevronLeft,
  chevronRight: GoChevronRight,
  chevronUp: GoChevronUp,
  close: GoX,
  edit: AiFillEdit,
  github: GoMarkGithub,
  darkMode: BsFillMoonStarsFill,
  lightMode: BsFillSunFill,
  menu: AiOutlineMenu,
  plus: MdAdd,
  settings: VscSettingsGear,
  tree: VscListTree,
  unknown: GrStatusUnknown
};

export type IconFactory = typeof icons;
export type IconFactoryKeys = keyof IconFactory;

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
