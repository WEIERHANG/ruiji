import React from 'react';
import {Box, Tabs, Tab, Typography, AppBar, CssBaseline} from '@mui/material';
import {BrowserRouter, Link, Routes, Route} from 'react-router-dom';
import SearchPage from './SearchPage';
import Favorites from './Favorites';

export default function App () {
    const [currentTab, setCurrentTab] = React.useState(0);
    const [searchResult, setSearchResult] = React.useState([]);


    const handleTabChange = (event, newValue) => {
        setCurrentTab(newValue);
    }

    return (
        <React.Fragment>
            <CssBaseline/>

            <AppBar position="fixed">
                <Typography align="center" variant="h3" color="inherit">Favorite Music</Typography>
            </AppBar>
            <div style={{height: 60, width: '100%'}}></div>
            {/* <BrowserRouter> */}
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={currentTab} onChange={handleTabChange} aria-label="basic tabs"
                    centered >
                  <Tab label="Search Music" value={0}  /* component={Link} to="/" */ />
                  <Tab label="Favorites" value={1}  /* component={Link} to="/favorites" */ />
                </Tabs>
            </Box>

           

          <Routes>
            <Route
              path="/"
              element={<SearchPage list={searchResult} onSearch={setSearchResult} onUpdate={handleSetFavorites} />}
            />
            <Route
              path="/favorites"
              element={<Favorites favorites={favorites} onUpdate={handleSetFavorites} />}
            />
          </Routes>
        {/* </BrowserRouter>÷ */}

           


            {/* { currentTab == 0 && <SearchPage list={searchResult} onSearch={setSearchResult}/> }
            { currentTab == 1 && <Favorites /> } */}

        </React.Fragment>
    )
}

