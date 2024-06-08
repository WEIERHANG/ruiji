import React from 'react';
import {Card, CardContent, CardActions, Typography, IconButton} from '@mui/material';
import {Favorite, FavoriteBorder} from '@mui/icons-material';
import SnackMsg from './SnackMsg';

const styles = {
    content : {},
    layout : {
        display : 'flex',
        justifyContent : 'center'
    },
    card: {
        minWidth: 275,
        maxWidth: 600,
        marginBottom : "20pt",
        marginLeft : 'auto',
        marginRight : 'auto',
    },
};

export default function MusicList ({list}) {
    const [likes, setLikes] = React.useState({});
    const [snackState, setSnackState] = React.useState({open : false, msg : ''})

    const toggleFavorite = (id, name) => () => {
        // React Hooks useState() with Object
        // https://stackoverflow.com/questions/54150783/react-hooks-usestate-with-object

        // state variables are read-only. You cannot update state variables directly.

        setLikes({...likes, [id] : !likes[id]}); 

        //snackState = { open : true, msg : `${id} is clicked`}
        setSnackState({...snackState, open : true, msg : `${name} is clicked` })
    }

    const handleSnackbarClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
          }
      
          setSnackState({open : false, msg : ''});
    }       

    return (
        <div>
            {list.map(item => {
                return (
                <Card sx={styles.card} key={item.collectionId}>
                    <CardContent>
                        <Typography variant="subtitle1"> {item.artistName}</Typography>
                        <Typography variant="subtitle2"> {item.collectionCensoredName}</Typography>
                    </CardContent>
                    <CardActions>
                        <IconButton  onClick={toggleFavorite(item.collectionId, item.collectionName)}>
                            {(likes[item.collectionId] === true) ? 
                                <Favorite /> : <FavoriteBorder /> }
                        </IconButton>
                    </CardActions>

                </Card>)
            })}
            <SnackMsg open = {snackState.open} message={snackState.msg} 
                onClose={handleSnackbarClose}/>
        </div>
    );
}
