{-# LANGUAGE BlockArguments #-}
{-# LANGUAGE RecordWildCards #-}

module Serial where

import GHC.TypeNats

import Linear.Vector
-- TODO: sized
import qualified Data.Vector as V
import qualified Data.Vector.Mutable as MV
import Linear.V
import Control.Monad.ST
import Linear (distance)

data RawData (d :: Natural) = RawData 
  { points    :: V.Vector (V d Double)
  , centroids :: V.Vector (V d Double) -- TODO: check mutable version
  } 

kMeans :: KnownNat d 
  => Int                   -- choosen k
  -> Int                   -- max_iterations
  -> V.Vector (V d Double) -- points 
  -> V.Vector (V d Double) -- output clusters
kMeans k n points = 
  let rawdata = initializeRawData k points
  in rawdata `convergeAtMostIn` n

convergeAtMostIn :: KnownNat d => RawData d -> Int -> V.Vector (V d Double)
RawData { centroids }`convergeAtMostIn` 0 = centroids 
r@(RawData _ cs) `convergeAtMostIn` n 
  | cs == cs' = cs
  | otherwise = r `convergeAtMostIn` (n-1)
  where 
    RawData _ cs' = updateCentroids r

-- TODO: State version or even ST for performance when?
updateCentroids :: KnownNat d => RawData d  -> RawData d  
updateCentroids rawdata@RawData {..} = runST do
  let k = V.length centroids
  sums   <- MV.replicate k zero
  counts <- MV.replicate k 0

  V.forM_ points \point -> do
    let closestIdx = V.minIndex (distance point <$> centroids)
    MV.modify sums   (^+^ point) closestIdx
    MV.modify counts (+ 1)       closestIdx

  cs <- V.generateM k \i -> do
    n <- MV.read counts i
    if n == 0
      then pure (centroids V.! i)
      else (^/ fromInteger n) <$> MV.read sums i

  pure rawdata { centroids = cs }

initializeRawData :: KnownNat d => Int -> V.Vector (V d Double) -> RawData d 
initializeRawData k points = runST do 
  sums   <- MV.replicate k zero
  counts <- MV.replicate k 0

  V.iforM_ points \i point -> do
    let i' = i `mod` k
    MV.modify sums (^+^ point) i'
    MV.modify counts (+ 1) i'

  cs <- V.generateM k \i -> 
    (^/) <$> MV.read sums i <*> MV.read counts i

  pure RawData { points    = points
               , centroids = cs 
               }

  



  
  


  
  
