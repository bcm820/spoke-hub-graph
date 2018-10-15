import React from 'react';
import { TransitSystem, toJS } from '../scala-js/transit-fastopt';
import { CtxProvider } from './Context';
import Display from './components/Display';
import Input from './components/Input';
import InfoPane from './components/InfoPane';

class App extends React.Component {
  state = {
    transitSys: TransitSystem,
    plotPoints: {},
    selected: false,
    cityId: null,
    netId: null,
    info: null,
    setCtx: obj => this.setState(obj),
    clearInfo: () => this.clearInfo()
  };

  /**
   * Constructs a new route to be added to the TransitSystem graph.
   */
  add = route => {
    this.clearInfo();
    const { transitSys } = this.state;
    transitSys.add(route);
    this.setState({ transitSys });
    route.split('-').forEach(c => this.plot(c.trim()));
  };

  /**
   * Generates a random route to be added to the TransitSystem graph.
   */
  generate = () => {
    this.clearInfo();
    const { transitSys } = this.state;
    const routeCities = toJS(transitSys.generate());
    this.setState({ transitSys });
    routeCities.forEach(this.plot);
  };

  /**
   * Assigns random coordinates to plot a city on the display.
   */
  plot = city => {
    this.setState(({ plotPoints }) => {
      return !plotPoints.hasOwnProperty(city)
        ? {
            plotPoints: {
              ...plotPoints,
              [city]: {
                label: city,
                symbol: 'circle',
                x: Math.floor(Math.random() * 500 + 1),
                y: Math.floor(Math.random() * 500 + 1)
              }
            }
          }
        : { plotPoints };
    });
  };

  /**
   * Clear information displayed in the InfoPane.
   */
  clearInfo = () => {
    this.setState({
      cityId: null,
      netId: null,
      info: null,
      selected: false,
      plotPoints: this.resetSymbol()
    });
  };

  /**
   * Reset's a deselected city's symbol.
   */
  resetSymbol = () => {
    const { plotPoints, cityId } = this.state;
    if (cityId) plotPoints[cityId].symbol = 'circle';
    return plotPoints;
  };

  render() {
    const { info } = this.state;
    return (
      <CtxProvider value={this.state}>
        <Input add={this.add} />
        <button className={'generate'} onClick={this.generate}>
          Generate Random
        </button>
        <Display />
        {info && <InfoPane />}
      </CtxProvider>
    );
  }
}

export default App;
