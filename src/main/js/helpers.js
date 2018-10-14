export const toRGB = netId => {
  const [r, g, b] = netId.split('.');
  return `rgb(${r}, ${g}, ${b})`;
};
